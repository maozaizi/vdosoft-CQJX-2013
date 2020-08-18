package com.u2a.framework.service.workflow;


import com.opensymphony.workflow.StoreException;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.spi.WorkflowEntry;

public interface WorkFlowHelper {
	public void createEntry(WorkflowEntry entry) throws StoreException;
	public void createCurrentStep(long entryId,String owner,Step step) throws StoreException;
	public void markFinished(Step step) throws StoreException;
	public void moveToHistory(Step step) throws StoreException;
	public void setEntryState(long id, int state) throws StoreException;
}
