import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
    public PrivateKey privatekey;
	public PublicKey publickey;
	
	public HashMap<String,TransactionsOutput> UTXOs = new HashMap<String,TransactionsOutput>();

    public Wallet(){
        generateKeyPair();

    }

    public void generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
	        	KeyPair keyPair = keyGen.generateKeyPair();
	        	// Set the public and private keys from the keyPair
	        	privatekey = keyPair.getPrivate();
	        	publickey = keyPair.getPublic();
		}catch(Exception e) {
			throw new RuntimeException(e);
        }
    }
        
        //returns balance and stores the UTXO's owned by this wallet in this.UTXOs
	public float getBalance() {
		float total = 0;	
        for (Map.Entry<String, TransactionsOutput> item: blockchainTESTE.UTXOs.entrySet()){
        	TransactionsOutput UTXO = item.getValue();
            if(UTXO.isMine(publickey)) { //if output belongs to me ( if coins belong to me )
            	UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
            	total += UTXO.value ; 
            }
        }  
		return total;
	}
	//Generates and returns a new transaction from this wallet.
	public Transactions sendFunds(PublicKey _recipient,float value ) {
		if(getBalance() < value) { //gather balance and check funds.
			System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}
    //create array list of inputs
		ArrayList<TransactionsInput> inputs = new ArrayList<TransactionsInput>();
    
		float total = 0;
		for (Map.Entry<String, TransactionsOutput> item: UTXOs.entrySet()){
			TransactionsOutput UTXO = item.getValue();
			total += UTXO.value;
			inputs.add(new TransactionsInput(UTXO.id));
			if(total > value) break;
		}
		
		Transactions newTransaction = new Transactions(publickey, _recipient , value, inputs);
		newTransaction.generateSignature(privatekey);
		
		for(TransactionsInput input: inputs){
			UTXOs.remove(input.transactionOutputId);
		}
		return newTransaction;
	}
	

}

