package com.u2a.framework.service.workflow;

import java.util.Date;

import com.brick.dao.IBaseDAO;
import com.opensymphony.workflow.StoreException;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.jdbc.JDBCWorkflowStore;

public class BrickJDBCWorkflowStore extends JDBCWorkflowStore {
	protected WorkFlowHelper help;
	protected IBaseDAO db;

	public WorkflowEntry createEntry(String workflowName) throws StoreException {
		WorkflowEntry entry=super.createEntry(workflowName);
		//help.createEntry(entry);
		return entry;
		
	}
	 public void setEntryState(long id, int state) throws StoreException {
		 super.setEntryState(id, state);
		 help.setEntryState(id, state);
	 }
	public Step createCurrentStep(long entryId, int wfStepId, String owner, Date startDate, Date dueDate, String status, long[] previousIds) throws StoreException {
		Step step=super.createCurrentStep(entryId, wfStepId, "", startDate, dueDate, status, previousIds);
		help.createCurrentStep(entryId,owner, step);
		return step;
		
	}
	public Step markFinished(Step step, int actionId, Date finishDate, String status, String caller) throws StoreException {
		Step s=super.markFinished(step, actionId, finishDate, status, caller);
		help.markFinished(step);
		return s;
		
	}
	public void moveToHistory(Step step) throws StoreException {
		super.moveToHistory(step);
		help.moveToHistory(step);
	}
	public WorkFlowHelper getHelp() {
		return help;
	}
	public void setHelp(WorkFlowHelper help) {
		this.help = help;
	}
	public IBaseDAO getDb() {
		return db;
	}
	public void setDb(IBaseDAO db) {
		this.db = db;
	}
}
