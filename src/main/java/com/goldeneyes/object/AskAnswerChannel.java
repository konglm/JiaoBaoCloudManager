package com.goldeneyes.object;

import java.io.Serializable;

public class AskAnswerChannel  implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer tabid;

    private String channelcode;

    private String channelname;

    private Integer upperid;

    private Byte status;

    private Integer orderid;

    public Integer getTabid() {
        return tabid;
    }

    public void setTabid(Integer tabid) {
        this.tabid = tabid;
    }

    public String getChannelcode() {
        return channelcode;
    }

    public void setChannelcode(String channelcode) {
        this.channelcode = channelcode == null ? null : channelcode.trim();
    }

    public String getChannelname() {
        return channelname;
    }

    public void setChannelname(String channelname) {
        this.channelname = channelname == null ? null : channelname.trim();
    }

    public Integer getUpperid() {
        return upperid;
    }

    public void setUpperid(Integer upperid) {
        this.upperid = upperid;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }
}