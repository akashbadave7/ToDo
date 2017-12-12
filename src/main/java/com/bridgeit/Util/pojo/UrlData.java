package com.bridgeit.Util.pojo;

public class UrlData {

		private String urlTitle;
		
		private String ulrImage;
		
		private String urlDomain;
		
		public UrlData(String urlTitle,String urlImage,String urlDomain)
		{
			this.urlTitle=urlTitle;
			this.ulrImage=urlImage;
			this.urlDomain=urlDomain;
		}

		public String getUrlTitle() {
			return urlTitle;
		}

		public void setUrlTitle(String urlTitle) {
			this.urlTitle = urlTitle;
		}

		public String getUlrImage() {
			return ulrImage;
		}

		public void setUlrImage(String ulrImage) {
			this.ulrImage = ulrImage;
		}

		public String getUrlDomain() {
			return urlDomain;
		}

		public void setUrlDomain(String urlDomain) {
			this.urlDomain = urlDomain;
		}
		
		
		
}
