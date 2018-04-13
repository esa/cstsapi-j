package esa.egos.csts.api.serviceinstance;

import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.time.ElapsedTimer;

public class ReturnPair {
	
	private IConfirmedOperation confOperation;
	private ElapsedTimer elapsedTimer;
	
    public ReturnPair(IConfirmedOperation confOperation, ElapsedTimer elapsedTimer)
    {
        this.setConfirmedOperation(confOperation);
        this.setElapsedTimer(elapsedTimer);
    }

	public ElapsedTimer getElapsedTimer() {
		return elapsedTimer;
	}

	public void setElapsedTimer(ElapsedTimer elapsedTimer) {
		this.elapsedTimer = elapsedTimer;
	}

	public IConfirmedOperation getConfirmedOperation() {
		return confOperation;
	}

	public void setConfirmedOperation(IConfirmedOperation confOperation) {
		this.confOperation = confOperation;
	}

}
