var getInfo = function() {
  $.ajax({
    url: '/painter/me',
    dataType: 'json',
    success: function(data) {
      $('#painteravatar').attr('src', '/avatar/' + data.avatar);
      $('#paintername').text(data.name);
      $('#myuid').val(data.id);
      $('#myid').text(data.userId);
      $('#myname').val(data.name);
      $('#mydesc').val(data.description);
    },
    error: function(jqr, status, err) {
      console.log(err);
    }
  });
};

var getAllWeeks = function() {
  $.ajax({
    url: '/week/all?order=desc',
    dataType: 'json',
    success: function(data) {
      $.each(data, function(i, w) {
        var option = $('<option>', {
          text: w.name + ' (' + w.startDate + ')',
          value: w.id
        });
        $('#newweek').append(option);
      });
    },
    error: function(jqr, status, err) {
      console.log(err);
    }
  });
};

var uploadMyAvatar = function() {
  if (!$('#myavatar').val()) {
    console.log('empty');
    return;
  }

  var fd = new FormData();
  fd.append('avatar', $('#myavatar')[0].files[0]);
  fd.append('pid', $('#myuid').val());

  $.ajax({
    url: '/painter/avatar',
    method: 'POST',
    data: fd,
    dataType: 'text',
    processData: false,
    contentType: false,
    success: function(result) {
      $('#painteravatar').attr('src', '/avatar/' + result + '?' + new Date().getTime());
      $('#uploadAvatarOK').fadeIn(400, function() {
        $('#uploadAvatarOK').fadeOut(1000);
      });
    },
    error: function(jqr, status, err) {
      console.log(err);
    }
  });
};

var updateBasicBtn = function() {
  var newname = $('#myname').val();
  var newdesc = $('#mydesc').val();
  if (!newname) {
    alert('Name cannot be empty');
    return;
  }

  $.ajax({
    url: '/painter/me',
    method: 'PUT',
    data: {
      newname: newname,
      newdesc: newdesc
    },
    dataType: 'json',
    success: function(result) {
      $('#updateBasicOK').fadeIn(400, function() {
        $('#updateBasicOK').fadeOut(1000);
      });
    },
    error: function(jqr, status, err) {
      console.log(err);
    }
  });
};

var newArtifactUpload = function() {
  var weekid = $('#newweek').val();
  var desc = $('#newdesc').val();
  if (!weekid || !$('#newartifact').val()) {
    alert('Painting not ready');
    return;
  }

  var fd = new FormData();
  fd.append('weekid', weekid);
  fd.append('desc', desc);
  fd.append('painterid', $('#myuid').val());
  fd.append('painting', $('#newartifact')[0].files[0]);

  $.ajax({
    url: '/painting/upload',
    method: 'POST',
    data: fd,
    dataType: 'json',
    processData: false,
    contentType: false,
    success: function(result) {
      $('#newartifactOK').fadeIn(400, function() {
        $('#newartifactOK').fadeOut(1000);
      });
    },
    error: function(jqr, status, err) {
      console.log(err);
    }
  });
};

var setupButton = function() {
  $('#uploadAvatarBtn').on('click', uploadMyAvatar);
  $('#updateBasicBtn').on('click', updateBasicBtn);
  $('#newartifactBtn').on('click', newArtifactUpload);
};

$(function() {
  getInfo();
  getAllWeeks();
  setupButton();
});