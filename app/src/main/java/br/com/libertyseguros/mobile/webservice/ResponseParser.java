package br.com.libertyseguros.mobile.webservice;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.com.libertyseguros.mobile.common.Util;

/*
	 * SAX parser to parse persons response
	 */
	public class ResponseParser extends DefaultHandler
	{
		// Result
		private String response;
		// string builder acts as a buffer
		private StringBuilder builder;
		// result will return
		private String localNameResult;
		
		/**
		 * Constructor
		 */
		public ResponseParser(String localNameResult) {
			super();
			this.localNameResult = localNameResult;
		}
		
		/**
		 * @return response
		 */
		public String getResponse() {
			return response;
		}


		/**
		 * Initialize the arraylist
		 * @throws SAXException
		 */
		@Override
		public void startDocument() throws SAXException {
			response = new String();
		}

		/**
		 * Initialize the temp person object which will hold the parsed info
		 * and the string builder that will store the read characters
		 * @param uri
		 * @param localName
		 * @param qName
		 * @param attributes
		 * @throws SAXException
		 */
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

			if(localName.equalsIgnoreCase(localNameResult)){
				builder = new StringBuilder();
			}

		}
		/**
		 * Finished reading the person tag, add it to arraylist
		 * @param uri
		 * @param localName
		 * @param qName
		 * @throws SAXException
		 */
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			
			// finished reading a person, add it to the arraylist
			if(localName.equalsIgnoreCase(localNameResult)){
				response = builder.toString();
			}
		}

		/**
		 * Read the value of each tag
		 * @param ch
		 * @param start
		 * @param length
		 * @throws SAXException
		 */
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			
			if(builder != null){
				// read the characters and append them to the buffer
				String tempString = new String(ch, start, length);
				builder.append(tempString);
			}
		}
		
	    @Override
	    protected void finalize() throws Throwable {
	    	Util.callGB();
	    	super.finalize();
	    }
	}