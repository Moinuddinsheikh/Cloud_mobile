$(document).ready(function () {
  	var qr = ' ';
  	$('#upload').on('click', function(e){
      	e.preventDefault();
    	$("#browse")[0].click();
    });
  
	$('#browse').change(function(){
    	var form = $("#uploadform");
        var formData = new FormData(form[0]);
      	for (var value of formData.values()) {
   			alert(value); 
		}
        $.ajax({
            type: "POST",
            url: "service.php",
            contentType: false,
            processData: false,
            data: formData,
            success: function (response) {
            	alert("file uploaded successfully!");
            }
        });
    });
  
    $('#qr_btn').click(function(){
      	$.ajax({
            type: "POST",
            url: "service.php",
          	data: {
            	action: "get_qrcode"
            },
            success: function (response) {
            	qr = response;
              	$('#qr_img').attr('src','https://chart.googleapis.com/chart?chs=300x300&cht=qr&chl='+qr);
            }
        });
        $('#illus').fadeOut();
        $('#info').fadeOut();
        $('#info_bg').fadeOut();
        $('#inst_bg').fadeIn();
        $('#inst').delay(500).fadeIn(1000);
        $('#qr_img').delay(500).fadeIn(1000);
        $('#inst_cls').delay(500).fadeIn(1000);
    });
  
  	$('#inst_cls').click(function(){
        $('#inst_bg').fadeOut();
        $('#inst').fadeOut();
        $('#qr_img').fadeOut();
        $('#inst_cls').fadeOut();
        $('#illus').fadeIn();
        $('#info').fadeIn();
        $('#info_bg').fadeIn();
    });
  	
  	if ($('#index_body').length > 0){
  		setInterval(() => qr_accept(qr) ,15000);
    }
  
});

function qr_accept(qr){
	$.ajax({
        type: "POST",
        url: "service.php",
        data: {
            action: "qr_accept",
            qrcode: qr
        },
        success: function (response) {
    		if (response.includes("success")){
              	// alert(qr);
              	var i = response.indexOf("-");
				var un = response.substring((i+1));
              	un = un.hexEncode();
            	window.location="http://13.234.78.165/~coderx/cloudMobile/files.php?username="+un;
            }
	    }
    });
}

String.prototype.hexEncode = function(){
    var hex, i;

    var result = "";
    for (i=0; i<this.length; i++) {
        hex = this.charCodeAt(i).toString(16);
        result += ("000"+hex).slice(-4);
    }
    return result
}

String.prototype.hexDecode = function(){
    var j;
    var hexes = this.match(/.{1,4}/g) || [];
    var back = "";
    for(j = 0; j<hexes.length; j++) {
        back += String.fromCharCode(parseInt(hexes[j], 16));
    }

    return back;
}