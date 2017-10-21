$('#password').focusin(function(){
  $('form').addClass('up')
});
$('#password').focusout(function(){
  $('form').removeClass('up')
});

// Panda Eye move
$(document).on( "mousemove", function( event ) {
  var dw = $(document).width() / 15;
  var dh = $(document).height() / 15;
  var x = event.pageX/ dw;
  var y = event.pageY/ dh;
  $('.eye-ball').css({
    width : x,
    height : y
  });
});

// validation


$('.btn').click(function(event){
    
    var pass= $("input[name='password']").val();
    var confirmPass = $("input[name='confirmPassword']").val();
    
   
    if(pass !==confirmPass){
        event.preventDefault();
        
        $(".mismatch").css("display","block");
        
        
        setTimeout(function(){ 
             $('.mismatch').css("display","none");
        },3000 );
        
        return;
        
    }
    
  $('form').addClass('wrong-entry');
  
    setTimeout(function(){ 
       $('form').removeClass('wrong-entry');
     },3000 );
});