
'use strict';

/*angular.module('app', ['ngRoute', '7minWorkout']).config(function ($routeProvider, $sceDelegateProvider) {
	$sceDelegateProvider.resourceUrlWhitelist([
	                                           'self', 'http://*.youtube.com/**']);
});*/

angular.module('myApp', [])
  .controller('MovieController', function($scope, $http) {
    var recentPictures = [];
    var favoriten = [];
    var retrieveTimer = 0;
    $scope.logged_in = false;
    var userid = 0;
    var username;
    $scope.favoriten = [];
    
    var apis = [
                {"name": "imgur", 
                 "recentImagesUrl": "https://api.imgur.com/3/gallery/hot/viral/0.json", 
                 "headers": {
	        	    headers: {'Authorization': 'Client-ID 2f81f2a55b01697'}
	        	},
	        	 "getNextImageUrl": function () {
	        		 return "https://api.imgur.com/3/gallery/hot/viral/" + retrieveTimer + ".json";
	        	 },
                 "successFunction": function (response) {
				    	
	        		$(response.data).each(function() {
	        			
	        			var bild = {"url": this.link, "beschreibung": this.title, "plattform": "imgur"};
	        			if (!this.is_album) {
	        				recentPictures.push(bild);
	        			}
	        			
	        		});
	                
			        	
                 }}/*,
                 {"name": "imageshack", 
                     "recentImagesUrl": "http://api.imageshack.com/v2/images", 
                     "headers": null,
    	        	 "getNextImageUrl": null,
                     "successFunction": function (response) {
    				    	
    	        		$(response.images).each(function() {
    	        			
    	        			var bild = {"url": "https://www.imageshack.com/i/" + this.id, "beschreibung": this.title, "plattform": "imageshack"};
    	        			recentPictures.push(bild);
    	        			
    	        			
    	        		});
    	                
    			        	
                     }}
                 
                 /*,{"name": "youtube", 
	                 "recentImagesUrl": "https://www.googleapis.com/youtube/v3/search?part=snippet&order=viewCount&type=video&maxResults=50&key=AIzaSyDargI4b41da5DqdPrOzNQio317_ZqreiI", 
	                 "changeFunction": function (response) {
	                 },
	                 "headers": null,
	                 "successFunction": function (response) {
					    	
		        		$(response.items).each(function() {
		        			
		        			var video = {"url": this.id.videoId, "beschreibung": this.snippet.title, "plattform": "youtube"};
		        			
		        			recentVideos.push(video);
		        			
		        		});
		                
				        	
	                 }}*/
              ];
    
    fetch();

    function fetch() {

    	$(apis).each(function() {
    		var api = this;
    		$http.get(api.recentImagesUrl, api.headers)
        	.success(function(response) {	        	
        		api.successFunction(response);
        		changeActiveImage(recentPictures[0]);
	        })
	        .error(function (data, status) {
	        	//console.error("ERROR", status, data);
	        })
	        .catch(function(response) {
	        	//console.error("ERROR", response);
	        });
    	});
    	
    	$scope.recentPictures = recentPictures;
    	
    }
    
    function writeErrorMessage(message) {
    	$(".errormessage").html(message);
    }
    
    function doLogin(response) {

    		if (response.result == "success") {
    			
    			$scope.logged_in = true;
    			userid = response.userid;
    			username = response.username;
    			$scope.username = username;
    			$(".loginUsername").hide();
    			$(".loginField").hide();
    			$(".logout").show();
    			$(".errormessage").text("");
    			writeErrorMessage(response.resultmessage + "<br>");
    			getFavoriten();
    		
    		} else {
    			writeErrorMessage(response.resultmessage + "<br>");
    		}
 
    	
    }
    
    $scope.register = function() {
    	$(".errormessage").text("");
		var usernames = jQuery("#username").val().trim();
		
		var request = $http({
            method: "post",
            url: "http://localhost:8888/user",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
		    transformRequest: function(obj) {
		        var str = [];
		        for(var p in obj) {
		        	str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));	
		        }
		        
		        return str.join("&");
		    },
            data: {
                username: usernames
            }
        });

    	request.success(function(response) {
    		doLogin(response);
    		
        });
    	
    }
    
	$scope.login = function() {
		$(".errormessage").text("");
		var usernames = jQuery("#username").val().trim();
    	$http.get("http://localhost:8888/user?username=" + usernames + "")
    	.success(function(response) {
			doLogin(response);
			$(".favoriteButton").toggle();
        });
		
    	
	}
	
    function getFavoriten() {
    	var username = jQuery("#username").val().trim();
    	$http.get("http://localhost:8888/favoriten?username=" + username + "")
    	.success(function(response) {
    		$(response.results).each(function() {
    			var bild = {"url": this.url, "beschreibung": this.beschreibung, "plattform": this.plattform};    		
    				favorithinzufuegen(bild);
    			
    		});
    		  
        });   	
    	
    }
    
    function favoritenloeschen() {
    	$(".favoritenContainer .imageDisplay").remove();
    }
    function favorithinzufuegen(bild) {
    	var string = '<div class="imageDisplay" style="height: 140px; width: 140px; max-height: 140px; max-width: 140px; float:right;"><div class="begrenzer" style="max-height: 140px; max-width: 140px; height: 140px; width: 140px; overflow:hidden; margin:0px;"><img style="min-width: 160px; max-width: 200px; min-height: 160px; max-height: 200px;" src="' 
    		+ bild.url + '"></div><div style="display: none;"><p class="beschreibung">' 
    		+ bild.beschreibung + '</p><p class="plattform"><a href="' 
    		+ bild.url + '">'
    		+ bild.plattform + '</a></p></div></div>'
    	$(".favoritenContainer").append(string);
    }
    
    $scope.postFavoriten = function () {
    	var usernameA = jQuery("#username").val().trim();
    	var request = $http({
    		
            method: "post",
            url: "http://localhost:8888/favoriten",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
 		    transformRequest: function(obj) {
 		        var str = [];
 		        for(var p in obj) {
 		        	str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));	
 		        }
 		        
 		        return str.join("&");
 		    },
            data: {
            	bildpattform: $(".activeImagePlattform a").text(),
                bildbeschreibung: $(".activeImageName").text(),
                bildurl: $(".activeImageContainer img").attr("src"),
                username: usernameA
            }
        });

    	request.success(function(response) {
    		if (response.result == "success") {
    			var bild = {"url": response.url, "beschreibung": response.beschreibung, "plattform": response.plattform};    		
    			favorithinzufuegen(bild);
    		}
    		
    		
        });
    	
    }
    
    function changeActiveImage(newActiveImage) {
     	
			$(".active").removeClass("active");
			$(newActiveImage).addClass("active");
			// move all information from this to the active thingy
			$(".activeImageName").html(newActiveImage.beschreibung);
			$(".activeImagePlattform").html('<a href="' + newActiveImage.url + '">' + newActiveImage.plattform + '</a>');
			$(".activeImageContainer img").attr("src", newActiveImage.url);
		
	}
    
    $scope.logout = function() {
    	
    	$scope.logged_in = false;
    	$(".loginUsername").show();
		$(".loginField").show();
		$(".logout").hide();
		$(".favoriteButton").toggle();
		$(".errormessage").text("");
		favoritenloeschen();
	
    }
    
   
  });
