var createWeek = function() {
  var weekname = $('#weekname').val();
  var weekdesc = $('#weekdesc').val();
  var weekdate = $('#weekdate').val();

  var fd = new FormData();
  fd.append('meta', '{"name": "' + weekname + '", "description": "' + weekdesc + '", "startDate": "' + weekdate + '"}');
  fd.append('photo', $('#weekphoto')[0].files[0]);

  $.ajax({
      url: '/week/create',
      method: 'POST',
      data: fd,
      dataType: 'json',
      processData: false,
      contentType: false,
      success: function(result) {
        $('#createWeekOK').fadeIn(400, function() {
          $('#createWeekOK').fadeOut(1000);
        });
      },
      error: function(jqr, status, err) {
        console.log(err);
      }
    });
};

var setupButton = function() {
  $('#weekdate').datepicker({
    language: 'zh-CN',
    format: 'yyyy-mm-dd'
  });
  $('#createWeekBtn').on('click', createWeek);
};

$(function() {
  setupButton();
});