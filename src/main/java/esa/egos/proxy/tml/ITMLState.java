package esa.egos.proxy.tml;

public interface ITMLState
{
    public void hlConnectReq(String respPortId);

    public void tcpConnectCnf();

    public void tcpConnectInd();

    public void tcpDataInd(TMLMessage msg);

    public void hlDisconnectReq();

    public void tcpDisconnectInd();

    public void delSLEPDUReq(TMLMessage pduMsg, boolean last);

    public void hlPeerAbortReq(int diagnostic);

    public void tcpUrgentDataInd();

    public void hlResetReq();

    public void tcpAbortInd();

    public void tcpTimeOut();

    public void tcpError(String message);

    public void tmsTimeout();

    public void hbrTimeout();

    public void hbtTimeout();

    public void cpaTimeout();

    public void manageBadFormMsg();
}
