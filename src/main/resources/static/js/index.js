var getLatestWeek = function() {
  $.ajax({
    url: '/week/latest',
    dataType: 'json',
    success: function(data) {
      if (data) {
        console.log(data);
      }
    },
    error: function(err) {
    }
  });
}

$(function() {
  getLatestWeek();
})