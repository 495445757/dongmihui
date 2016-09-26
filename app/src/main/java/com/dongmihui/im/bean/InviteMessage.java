/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dongmihui.im.bean;

public class InviteMessage {
	private String from;
	private long time;
	private String reason;

	private InviteMesageStatus status;
	private String groupId;
	private String groupName;
	private String groupInviter;
	

	private int id;
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}


	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public InviteMesageStatus getStatus() {
		return status;
	}

	public void setStatus(InviteMesageStatus status) {
		this.status = status;
	}

	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public void setGroupInviter(String inviter) {
	    groupInviter = inviter;
	}
	
	public String getGroupInviter() {
	    return groupInviter;	    
	}



	public enum InviteMesageStatus{
	    
	    //==contact
		/**被邀请*/
		BEINVITEED,
		/**被拒绝*/
		BEREFUSED,
		/**被同意*/
		BEAGREED,
		
		//==group application
		/**远程用户申请加入*/
		BEAPPLYED,
		/**你同意加入*/
		AGREED,
		/**你拒绝了请求*/
		REFUSED,
		
		//==group invitation
		/**收到群邀请**/
		GROUPINVITATION,
		/**用户接收邀请**/
		GROUPINVITATION_ACCEPTED,
        /**用户拒绝邀请**/
		GROUPINVITATION_DECLINED
	}
	
}



