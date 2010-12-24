/*
 *  soapUI, copyright (C) 2004-2010 eviware.com 
 *
 *  soapUI is free software; you can redistribute it and/or modify it under the 
 *  terms of version 2.1 of the GNU Lesser General Public License as published by 
 *  the Free Software Foundation.
 *
 *  soapUI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU Lesser General Public License for more details at gnu.org.
 */

package com.eviware.soapui.security.registry;

import com.eviware.soapui.config.SQLInjectionCheckConfig;
import com.eviware.soapui.config.SecurityCheckConfig;
import com.eviware.soapui.security.check.SQLInjectionCheck;
import com.eviware.soapui.security.check.SecurityCheck;

/**
 * Factory for creation GroovyScript steps
 * 
 * @author soapUI team
 */

public class SQLInjectionCheckFactory extends AbstractSecurityCheckFactory {
	
	public SQLInjectionCheckFactory() {
		super(SQLInjectionCheck.TYPE, "SQLInjectionCheck",
				"Preforms a check for SQL Injection Vulerabilities",
				"/sql_injection_check_script.gif");
	}

	public boolean canCreate() {
		return true;
	}

	@Override
	public SecurityCheck buildSecurityCheck(SecurityCheckConfig config) {
		return new SQLInjectionCheck(config, null, null);
	}

	@Override
	public SecurityCheckConfig createNewSecurityCheck(String name) {
		SecurityCheckConfig securityCheckConfig = SecurityCheckConfig.Factory
				.newInstance();
		securityCheckConfig.setType(SQLInjectionCheck.TYPE);
		securityCheckConfig.setName(name);
		SQLInjectionCheckConfig sic = SQLInjectionCheckConfig.Factory.newInstance();
		securityCheckConfig.setConfig(sic);
		return securityCheckConfig;
	}

	@Override
	public boolean isHttpMonitor() {
		return false;
	}
}