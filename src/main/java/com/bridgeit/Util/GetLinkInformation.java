package com.bridgeit.Util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.bridgeit.Util.pojo.UrlData;

public class GetLinkInformation {

	public UrlData getMetaData(String url) throws IOException {
		 StringBuilder sb = new StringBuilder();
		String urlTitle=null;
		String urlImage=null;
		String urlDomain=null;
		
		try {
			URI uri=new URI(url);
			urlDomain=uri.getHost();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connection connection = Jsoup.connect(url);
		Document document = connection.get();
		Elements metaOgTitle = document.select("meta[property=og:title]");
	    if (metaOgTitle!=null) {
	        urlTitle = metaOgTitle.attr("content");
	    }
	    else {
	        urlTitle = document.title();
	    }
		
	    Elements metaOgImage = document.select("meta[property=og:image]");
	    if (metaOgImage!=null) {
	        urlImage = metaOgImage.attr("content");
	        System.out.println("imageUrl:"+urlImage);
	    }

	    if (urlImage!=null) {
	        sb.append("<img src='");
	        sb.append(urlImage);
	        sb.append("' align='left' hspace='12' vspace='12' width='150px'>");
	    }

	    if (urlTitle!=null) {
	        sb.append(urlTitle);
	    }
		return new UrlData(urlTitle,urlImage,urlDomain);
		
	}
}
