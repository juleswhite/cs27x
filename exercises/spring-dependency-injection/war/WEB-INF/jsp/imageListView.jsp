<!DOCTYPE html>
<html lang="en" ng-app="di">
  <head>
    <meta charset="utf-8">
    <title>CS 278</title>
   
    <script src="/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript">
    	
    	$.get("imageList",function(rslt){
    		for(var i in rslt){
    			var photo = rslt[i];
    			var id = photo.id;
    			var url = "image?id=" + id;
    			$("#images").append("<img src='"+url+"'></img>");
    		}
    	});
    	
    </script>
  </head>
  <body>

    <div id="images"></div>

   
  </body>
</html>
