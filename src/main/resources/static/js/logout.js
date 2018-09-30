$(function() {
  $.ajax({
    url: '/painter/logout',
    complete: function(jqr, status) {
      window.location.replace('/login.html');
    }
  });
});