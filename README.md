# SocialConnectorLibrary
This library can perform login , sharing and app invite features on facebook, google plus and twitter. creating a application on facebook, google and twitter developer console and then after add this library in your project and write a single lines of code to perform three operation login, sharing and app invite on facebok , google plus and twitter,






Remember: Enable Google Sign In for this application for use of google login. link "https://developers.google.com/mobile/add?platform=android&cntapi=signin&cnturl=https:%2F%2Fdevelopers.google.com%2Fidentity%2Fsign-in%2Fandroid%2Fsign-in%3Fconfigured%3Dtrue&cntlbl=Continue%20Adding%20Sign-In"








For facebook login
	
	
             socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.FACEBOOK, ConnectionType.LOGIN)
                              .setSocialConnection(this).build();
					
					
					
					
      
For google login
	  
              socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.GPLUS, ConnectionType.LOGIN)
                                 .setSocialConnection(this).build();
					
					
					
					
					

For twitter login
       
              socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.TWITTER, ConnectionType.LOGIN)
                               .setConsumerKey("enter consumer key")
                                .setConsumerSecret("enter consumer secret").setSocialConnection(this).build();
				
				
				
				
				
				
				
For facebook app invite

	   socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.FACEBOOK,                       				     ConnectionType.APPINVITE).setSocialConnection(this)
                             .setAppLinkUrl("enter facebook applink url")
                             .setUrl("enter application icon url")
                    .setAppName("enter application name").setDescription("enter application description").build();
					
					
					
					
										
For google plus invite			

            socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.GPLUS, ConnectionType.APPINVITE)
                             .setSocialConnection(this)
                             .setAppName("enter application name")
                             .setUrl("enter application web url or icon url")
                             .setAppLinkUrl("enter application install link url")
                             .setDescription("enter application description").build();
					
					
					
					
					
For twitter app invite				
        
            socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.TWITTER, ConnectionType.APPINVITE)
                              .setSocialConnection(this)
                             .setAppName("enter application name")
                             .setDescription("enter application description")
                             .setResourceId("enter application icon resource id")
                             .setAppLinkUrl("enter application install link url")
                             .setConsumerKey("enter twitter consumer key")
                             .setConsumerSecret("enter twitter consumer secret")
                             .setDescription("enter application description").build();	
			     
			     
			     
			     
			     
			     
For facebook sharing

                socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.FACEBOOK,  							  ConnectionType.POSTSHARE)
                                 .setSocialConnection(MainActivity.this)
                                 .setUrl("enter image url").setCaption("enter post caption").setDescription("enter post          					 description").build();
					
					
					
					
					
					
					
For google plus sharing
		
		         socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.GPLUS, 				                            ConnectionType.POSTSHARE)
                                            .setUrl("enter file uri string")
                                            .setCaption("enter post caption")
                                            .setDescription("enter post description")
                                             .setSocialConnection(MainActivity.this).build();
						
						
						
						
						
						
For twitter sharing			


                socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.TWITTER, 							    ConnectionType.POSTSHARE)
                                  .setConsumerKey("enter twitter consumer key")
                                  .setConsumerSecret("enter twitter consumer secret").setSocialConnection(MainActivity.this)
                                  .setUrl("enter file uri string").setCaption("enter post caption").setDescription("enter post                                             description").build();	
				  
				  
				  
				  
				  
And call onActivityResult() method
		
					
		@Override
                protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                   super.onActivityResult(requestCode, resultCode, data);
                     if (socialConnector != null)
                        socialConnector.onActivityResult(requestCode, resultCode, data)
                }		




			
					
