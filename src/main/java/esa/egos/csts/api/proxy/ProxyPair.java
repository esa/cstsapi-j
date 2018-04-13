package esa.egos.csts.api.proxy;

import esa.egos.csts.api.enums.AppRole;

public class ProxyPair
{
    private IProxyAdmin proxy;

    private AppRole role;


    public ProxyPair(IProxyAdmin proxy, AppRole role)
    {
        this.proxy = proxy;
        this.role = role;
    }

    public IProxyAdmin getProxy()
    {
        return this.proxy;
    }

    public void setProxy(IProxyAdmin proxy)
    {
        this.proxy = proxy;
    }

    public AppRole getRole()
    {
        return this.role;
    }

    public void setRole(AppRole bindRole)
    {
        this.role = bindRole;
    }
}
