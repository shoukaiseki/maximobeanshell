import java.rmi.RemoteException;

import org.shoukaiseki.app.autobsh.AutoBeanShell;
import org.shoukaiseki.expand.StringExpand;
import psdi.mbo.*;
import psdi.util.MXApplicationException;
import psdi.util.MXException;


/**
 * bshTest <br>
 *
 * @author 蒋カイセキ    Japan-Tokyo  2017-06-12 12:54:26<br>
 *         ブログ http://shoukaiseki.blog.163.com/<br>
 *         E-メール jiang28555@Gmail.com<br>
 **/

public class bshTest extends AutoBeanShell {
	Mbo absMbo=this;
	public bshTest(MboSet ms) throws RemoteException {
		super(ms);
		// TODO Auto-generated constructor stub
		new ALNDomain(null);
		psdi.app.ticket.FldTkReportedBy asd;
		psdi.app.common.purchasing.FldPurPreTaxTotal sadfs;
	}
	
	public void del() throws RemoteException, MXException{
		String status = absMbo.getString("STATUS");
		String personid = absMbo.getUserInfo().getPersonId();
		//登录人为工单管理员时不做只读处理
		if(!StringExpand.binarySearch(new String[]{"WOADMIN"}, personid)){
			//状态不为新建时做只读控制
			if(!StringExpand.binarySearch(new String[]{"等待批准","退回修改",""}, status)){
				absMbo.getMboSet("PRLINE").setFlag(7L, true);
				absMbo.setFieldFlag(new String[]{"S_MONTH","S_PROFESSION","S_PRTYPE","DESCRIPTION"
						,"SUPERVISOR","CURRENCYCODE","PRIORITY","ISSUEDATE","REQUIREDDATE","REQUESTEDBY"
						,"CONTRACTREFNUM","S_YEAR"}, 7L, true);
			}else{
				//新建的单子,登录人不为新建人做只读控制
				if(!StringExpand.binarySearch(new String[]{personid},absMbo.getString("REQUESTEDBY"))){
					absMbo.getMboSet("PRLINE").setFlag(7L, true);
					absMbo.setFieldFlag(new String[]{"S_MONTH","S_PROFESSION","S_PRTYPE","DESCRIPTION"
							,"SUPERVISOR","CURRENCYCODE","PRIORITY","ISSUEDATE","REQUIREDDATE","REQUESTEDBY"
							,"CONTRACTREFNUM","S_YEAR"}, 7L, true);
				}
			}
		}
	}
	
	@Override
	public void add() throws MXException, RemoteException {
		// TODO Auto-generated method stub
		super.add();
		absMbo.setValue("SITEID", absMbo.getUserInfo().getInsertSite(),11L);
		SqlFormat sqlformat=new SqlFormat("personid=:1");
		sqlformat.setObject(1, "PERSON", "PERSONID", absMbo.getUserInfo().getPersonId());
		MboSetRemote mboSet = absMbo.getMboSet("$tichuren_person", "person", sqlformat.format());
		mboSet.reset();
		if(mboSet.count()==1){
			MboRemote mbo = mboSet.getMbo(0);
			absMbo.setValue("DEPNUM", mbo.getString("DEPARTMENT"),2L);
			absMbo.setValue("ZHIBIE", mbo.getString("BANZU"),2L);
		}
		mboSet.close();
		
	}
	
	
	private void asus() throws RemoteException, MXException {
		// TODO Auto-generated method stub
		String status = absMbo.getString("STATUS");
		String personid = absMbo.getUserInfo().getPersonId();
		//登录人为工单管理员时不做只读处理
		if(!StringExpand.binarySearch(new String[]{"WOADMIN","MAXADMIN"}, personid)){
			absMbo.setFieldFlag(new String[]{"STATUS"},MboConstants.READONLY,true);
			if(StringExpand.binarySearch(new String[]{"CLOSE","已关闭","关闭"},status)){
				absMbo.setFieldFlag(new String[]{"HQBM","PERSONID001","DESCRIPTION","CONTRACTMANAGEMENTNUM","BANKACCOUNT","BANKNUM"},MboConstants.READONLY,true);
				absMbo.getMboSet("CONTRACTPAYMENTLINE").setFlag(7L, true);;
				absMbo.getMboSet("DOCLINKS").setFlag(7L, true);;
			}
			if(!StringExpand.binarySearch(new String[]{"NEW","","THXG"},status)){
				absMbo.setFieldFlag(new String[]{"HQBM","PERSONID001","DESCRIPTION","CONTRACTMANAGEMENTNUM","BANKACCOUNT","BANKNUM"},MboConstants.READONLY,true);
				absMbo.getMboSet("CONTRACTPAYMENTLINE").setFlag(7L, true);;
				absMbo.getMboSet("DOCLINKS").setFlag(7L, true);;
			}
		}

	}


	private Object aaaa001() throws Exception{
		if(StringExpand.binarySearch(new String[]{"新建","等待批准"},absMbo.getString("STATUS"))){

			return new MXApplicationException("#","只有状态为新建才能发送工作流");
		}

		return null;

	}

}
