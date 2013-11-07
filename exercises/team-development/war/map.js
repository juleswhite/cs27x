var map = null;

var refreshInterval = 1000;
var autoRefresh = true;
var markers = [];

function initialize() {
	var myOptions = {
		center : new google.maps.LatLng(37.2150,-80.4098),
		zoom : 14,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};

	map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);

	refreshResults();
}

function refreshResults(){
	
	if(map){
		var newcenter = map.getCenter();
		$.get("/data/nearby?lat=" + newcenter.lat() + "&lon=" + newcenter.lng()
			+ "&maxDist=0", handleResults);
	}
	
	if(autoRefresh){
		setTimeout(refreshResults, refreshInterval);
	}
}

function handleResults(result) {
	var uavs = result.data;
	
	var i = 0;
	for (i = 0; i < markers.length; i++){
		markers[i].setMap(null);
	}
	
	markers = [];
	
	for (i = 0; i < uavs.length; i++) {

		var uav = uavs[i];

		var location = new google.maps.LatLng(uav.location.lat,
				uav.location.lon);

		var marker = new google.maps.Marker({
			position : location,
			map : map,
			icon : '/uav2.png'
		});

		marker.setTitle(uav.description);

		markers.push(marker);
		
		var uavinfo = uav.description;
		attachListeners(uavinfo,map,marker);
	}
}

function attachListeners(uavinfo, map, marker){
		var infowindow = new google.maps.InfoWindow({
			content : uavinfo,
			size : new google.maps.Size(50, 50)
		});
		
		google.maps.event.addListener(marker, 'click', function() {
			infowindow.open(map, marker);
		});
}