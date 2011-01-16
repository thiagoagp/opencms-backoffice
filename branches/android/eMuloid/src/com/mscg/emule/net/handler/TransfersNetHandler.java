package com.mscg.emule.net.handler;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.htmlcleaner.CleanerProperties;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mscg.emule.bean.CategoryBean;
import com.mscg.emule.util.Constants;
import com.mscg.io.InputStreamDataReadListener;

public class TransfersNetHandler extends GenericSpeedInfoNetHandler {

	private Pattern categoryIDPattern = Pattern.compile(".*cat=(-?\\d+).*");

	public TransfersNetHandler(Handler handler, boolean localCache) {
		super(handler, localCache);
	}

	public TransfersNetHandler(Handler handler, CleanerProperties props, boolean localCache, InputStreamDataReadListener dataListener) {
		super(handler, props, localCache, dataListener);
	}

	public TransfersNetHandler(Handler handler, CleanerProperties props) {
		super(handler, props);
	}

	public TransfersNetHandler(Handler handler) {
		super(handler);
	}

	@Override
	public void handleDocument(Document document) throws Exception {
		Message m = null;
		// read the available categories
		NodeList downloadBoxTmp = (NodeList)xpath.evaluate("//table[./tbody/tr/td[@class='smallheader']][position()=1]/tbody", document, XPathConstants.NODESET);
		Node downloadBox = downloadBoxTmp.item(0);
		NodeList menuItems = (NodeList)xpath.evaluate("tr[position()=1]/td[position()=1]//div[@class='menuitems']//a", downloadBox, XPathConstants.NODESET);
		NodeList catLinks = (NodeList)xpath.evaluate("tr[position()=1]/td[position()=1]//div[@class='menuitems']//a/@href", downloadBox, XPathConstants.NODESET);
		NodeList catSelStatuses = (NodeList)xpath.evaluate("tr[position()=1]/td[position()=1]//div[@class='menuitems']//a/img/@src", downloadBox, XPathConstants.NODESET);
		List<CategoryBean> categories = new LinkedList<CategoryBean>();
		for(int i = 0, l = menuItems.getLength(); i < l; i++) {
			Node menuItem = menuItems.item(i);
			String catLink = catLinks.item(i).getTextContent();
			Matcher catIDMatcher = categoryIDPattern.matcher(catLink);
			if(catIDMatcher.find()) {
				try {
					Integer catID = Integer.parseInt(catIDMatcher.group(1));
					String catName = menuItem.getTextContent().trim();//Util.getNodesText((NodeList)xpath.evaluate("text()", menuItem, XPathConstants.NODESET)).trim();
					String catSelTxt = catSelStatuses.item(i).getTextContent();

					categories.add(new CategoryBean(
						catName, catID, catSelTxt.contains("checked.gif")));
				} catch(Exception e) {
					Log.w(this.getClass().getCanonicalName(), "Cannot create category bean.", e);
				}
			}
		}
		m = handler.obtainMessage(Constants.Messages.Transfers.UPDATE_CATEGORIES);
		m.obj = categories;
		handler.sendMessage(m);

		// read the downloads lines
		NodeList downloadLines = (NodeList)xpath.evaluate("tr[./td[starts-with(./@class, 'down-line')]]", downloadBox, XPathConstants.NODESET);
		for(int i = 0, l = downloadLines.getLength(); i < l; i++) {
			Node downloadLine = downloadLines.item(i);
		}

		m = handler.obtainMessage(Constants.Messages.UPDATE_TERMINATED);
		handler.sendMessage(m);
	}

}
