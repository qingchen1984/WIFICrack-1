package net.nym.wificrack.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * @author nym
 * @date 2015/10/12 0012.
 * @since 1.0
 */
public class WifiAdmin {
    //定义一个WifiManager对象
    private WifiManager mWifiManager;
    //定义一个WifiInfo对象
    private WifiInfo mWifiInfo;
    //扫描出的网络连接列表
    private List<ScanResult> mWifiList;
    //网络连接列表
    private List<WifiConfiguration> mWifiConfigurations;
    WifiManager.WifiLock mWifiLock;
    public WifiAdmin(Context context){
        //取得WifiManager对象
        mWifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //取得WifiInfo对象
        mWifiInfo=mWifiManager.getConnectionInfo();
    }

    public boolean isWifiEnabled(){
        return mWifiManager.isWifiEnabled();
    }

    //打开wifi
    public void openWifi(){
        if(!mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(true);
        }
    }
    //关闭wifi
    public void closeWifi(){
        if(mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(false);
        }
    }
    // 检查当前wifi状态
    public int checkState() {
        return mWifiManager.getWifiState();
    }
    //锁定wifiLock
    public void acquireWifiLock(){
        mWifiLock.acquire();
    }
    //解锁wifiLock
    public void releaseWifiLock(){
        //判断是否锁定
        if(mWifiLock.isHeld()){
            mWifiLock.acquire();
        }
    }
    //创建一个wifiLock
    public void createWifiLock(){
        mWifiLock=mWifiManager.createWifiLock("test");
    }
    //得到配置好的网络
    public List<WifiConfiguration> getConfiguration(){
//        return mWifiConfigurations;
        return mWifiManager.getConfiguredNetworks();
    }
    //指定配置好的网络进行连接
    public void connetionConfiguration(int index){
        if(index>mWifiConfigurations.size()){
            return ;
        }
        //连接配置好指定ID的网络
        mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId, true);
    }

    //指定配置好的网络进行连接
    public void connetionConfiguration(WifiConfiguration wifiConfiguration){
        if (wifiConfiguration == null){
            return;
        }
        //连接配置好指定ID的网络
        mWifiManager.enableNetwork(wifiConfiguration.networkId, true);
    }
    public void startScan(){
        mWifiManager.startScan();
        //得到扫描结果
        mWifiList=mWifiManager.getScanResults();
        //得到配置好的网络连接
        mWifiConfigurations=mWifiManager.getConfiguredNetworks();
    }
    //得到网络列表
    public List<ScanResult> getWifiList(){
        return mWifiList;
    }
    //查看扫描结果
    public StringBuffer lookUpScan(){
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<mWifiList.size();i++){
            sb.append("Index_" + new Integer(i + 1).toString() + ":");
            // 将ScanResult信息转换成一个字符串包
            // 其中把包括：BSSID、SSID、capabilities、frequency、level
            sb.append((mWifiList.get(i)).toString()).append("\n");
        }
        return sb;
    }
    public String getMacAddress(){
        mWifiManager.reconnect();
        mWifiInfo=mWifiManager.getConnectionInfo();
        return (mWifiInfo==null)?"NULL":mWifiInfo.getMacAddress();
    }
    public String getBSSID(){
        mWifiManager.reconnect();
        mWifiInfo=mWifiManager.getConnectionInfo();
        return (mWifiInfo==null)?"NULL":mWifiInfo.getBSSID();
    }
    public int getIpAddress(){
        mWifiManager.reconnect();
        mWifiInfo=mWifiManager.getConnectionInfo();
        return (mWifiInfo==null)?0:mWifiInfo.getIpAddress();
    }
    //得到连接的ID
    public int getNetWordId(){
        mWifiManager.reconnect();
        mWifiInfo=mWifiManager.getConnectionInfo();
        return (mWifiInfo==null)?0:mWifiInfo.getNetworkId();
    }
    //得到wifiInfo的所有信息
    public String getWifiInfo(){
        mWifiManager.reconnect();
        mWifiInfo=mWifiManager.getConnectionInfo();
        return (mWifiInfo==null)?"NULL":mWifiInfo.toString();
    }
    //添加一个网络并连接
    public boolean addNetWork(WifiConfiguration configuration){
        int wcgId=mWifiManager.addNetwork(configuration);
        configuration.networkId = wcgId;
        return mWifiManager.enableNetwork(wcgId, true);
    }

    //删除已保存的网络
    public boolean removeConnectionWifi(int netId){
       return mWifiManager.removeNetwork(netId);
    }

    //断开指定ID的网络
    public void disConnectionWifi(int netId){
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }
}