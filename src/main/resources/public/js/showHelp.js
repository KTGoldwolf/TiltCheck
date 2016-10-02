
$( document ).ready(function() {
    $('#aboutHeader').click(function() {
      $('#aboutContent').slideToggle("slow");
    });
    $('aboutHeader').hover( function() {
      $('#aboutHeader').css('cursor', 'pointer');
    });
});
