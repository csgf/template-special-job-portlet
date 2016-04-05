package it.dfa.unict;

import java.util.Arrays;

import it.infn.ct.GridEngine.Job.InfrastructureInfo;

public class AppInfrastructureInfo extends InfrastructureInfo {

	private String id;
	private boolean enableInfrastructure;
	private boolean PxServerSecure;
	private boolean pxRobotRenewalFlag;

	public AppInfrastructureInfo() {
		super("", "", "", null, "", "", "", "", "", false);
		this.enableInfrastructure = true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isEnableInfrastructure() {
		return enableInfrastructure;
	}

	public void setEnableInfrastructure(boolean enableInfrastructure) {
		this.enableInfrastructure = enableInfrastructure;
	}

	public boolean isPxServerSecure() {
		return PxServerSecure;
	}

	public void setPxServerSecure(boolean pxServerSecure) {
		PxServerSecure = pxServerSecure;
	}

	public boolean isPxRobotRenewalFlag() {
		return pxRobotRenewalFlag;
	}

	public void setPxRobotRenewalFlag(boolean pxRobotRenewalFlag) {
		this.pxRobotRenewalFlag = pxRobotRenewalFlag;
	}

	@Override
	public String toString() {
		return "AppInfrastructureInfo [id=" + id + ", enableInfrastructure="
				+ enableInfrastructure + ", PxServerSecure=" + PxServerSecure
				+ ", pxRobotRenewalFlag=" + pxRobotRenewalFlag + ", getName()="
				+ getName() + ", getBDII()=" + getBDII()
				+ ", getResourcemanagerList()="
				+ Arrays.toString(getResourcemanagerList())
				+ ", getUserProxy()=" + getUserProxy() + ", getETokenServer()="
				+ getETokenServer() + ", getETokenServerPort()="
				+ getETokenServerPort() + ", getProxyId()=" + getProxyId()
				+ ", getVO()=" + getVO() + ", getFQAN()=" + getFQAN()
				+ ", getSWTag()=" + getSWTag() + ", getMiddleware()="
				+ getMiddleware() + ", getKeystore()=" + getKeystore()
				+ ", getUserName()=" + getUserName() + ", getPassword()="
				+ getPassword() + ", getCEList()="
				+ Arrays.toString(getCEList()) + ", getRFC()=" + getRFC() + "]";
	}

}
