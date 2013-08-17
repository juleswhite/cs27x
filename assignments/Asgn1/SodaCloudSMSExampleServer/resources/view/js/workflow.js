angular.module('sms', ['ui.bootstrap']);

var rootController = {};

function connect($scope){
	

	Soda.connect("ws://"+$scope.host+":8081/sms/",function(){
		var uri = Soda.namingSvc.publish({
			smsEvent:function(evt){
				var msg = {from:evt.sms.from,content:evt.sms.content,to:'self'};
				$scope.msgs.unshift(msg);
				$scope.threads = createThreads($scope.msgs);
				$scope.setActiveThread(evt.sms.from);
				$scope.$digest();
			},
			smsSenderAdded:function(sender){
				$scope.smsSender=sender;
				$scope.sendingEnabled=true;
				$scope.$digest();
			}
		});
		
//		Uncomment the method below to manually test
//
		rootController.inSms = function(from,to,content){
			var msg = {from:from,content:content,to:to};
			$scope.sendingEnabled=true;
			$scope.msgs.unshift(msg);
			$scope.threads = createThreads($scope.msgs);
			var thread = (msg.from == 'self')? msg.to : msg.from;
			$scope.setActiveThread(thread);
			$scope.$digest();
	    };
		
		$scope.objRef = uri;
		$scope.encodedObjRef = encodeURIComponent(uri.uri);
		$scope.$digest();
	});
}


function createThreads(msgs){
	var threads = {};
	for(var i in msgs){
		var msg = msgs[i];
		var other = (msg.from == 'self')? msg.to : msg.from;
		if(!threads[other]){
			threads[other] = [];
		}
		
		threads[other].push(msg);
	}
	
	return threads;
}


function workflowController($scope) {
	$scope.host = window.location.hostname;
	
	$scope.msgs = [];
	
	$scope.threads = createThreads($scope.msgs);
	$scope.activeThread = "";
	
	rootController.msgs = $scope.msgs;
	rootController.threads = $scope.threads;
	
	$scope.setActiveThread = function(thread){
		$scope.activeThread = thread;
	};
	
	$scope.addThread = function(thread){
		$scope.threads[thread] = [];
		$scope.activeThread = thread;
	};
	
	$scope.filterByActiveThread = function(msg) {
        return msg.from == $scope.activeThread || msg.to == $scope.activeThread;
    };
	
	rootController.addThread = $scope.addThread;
	
	
	$scope.sendReply = function(){
		var to = $scope.activeThread;
		var msg = $scope.reply;
		var msgobj = {from:'self',to:to,content:msg};
		
		console.log("send:["+to+"] - "+msg);
	
		$scope.smsSender.send(to,msg);
		$scope.reply = "";
		
		$scope.msgs.unshift(msgobj);
		$scope.threads = createThreads($scope.msgs);
		$scope.$digest();
	};
	
    connect($scope);
}


function DialogDemoCtrl($scope, $dialog){

  $scope.opts = {
    backdrop: true,
    keyboard: true,
    backdropClick: true,
    templateUrl:  'widgets/addThread.html',
    controller: 'TestDialogController'
  };

  $scope.openDialog = function(){
    var d = $dialog.dialog($scope.opts);
    d.open().then(function(result){
      if(result)
      {
    	rootController.addThread(result);
      }
    });
  };
}

function TestDialogController($scope, dialog){
  $scope.close = function(result){
    dialog.close(result);
  };
}



