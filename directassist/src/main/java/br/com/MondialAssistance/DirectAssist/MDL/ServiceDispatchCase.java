package br.com.MondialAssistance.DirectAssist.MDL;

public class ServiceDispatchCase {

	private Case CaseResult;
	private ServiceDispatch serviceDispatch;
	private Provider provider;
	
	public Case getCase(){
		return CaseResult;
	}
	public void setCase(Case value){
		CaseResult = value;
	}
	
	public ServiceDispatch getServiceDispatch(){
		return serviceDispatch;
	}
	public void setServiceDispatch(ServiceDispatch value){
		serviceDispatch = value;
	}

	public Provider getProvider(){
		return provider;
	}
	public void setProvider(Provider value){
		provider = value;
	}
}
