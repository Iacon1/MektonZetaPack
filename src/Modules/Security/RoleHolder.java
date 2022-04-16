// By Iacon1
// Created 11/17/2021
// An entity that has roles

package Modules.Security;

import java.util.List;

public interface RoleHolder
{
	public void addRole(String role);
	public boolean hasRole(String role);
	public List<String> getRoles();
}
