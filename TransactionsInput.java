

public class TransactionsInput {
	public String transactionOutputId; //Reference to TransactionOutputs -> transactionId
	public TransactionsOutput UTXO; //Contains the Unspent transaction output
	
	public TransactionsInput(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}
}
