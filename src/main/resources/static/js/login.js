var validate = function(input) {
  if ($(input).attr('type') == 'email' || $(input).attr('name') == 'email') {
    if($(input).val().trim().match(/^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{1,5}|[0-9]{1,3})(\]?)$/) == null) {
        return false;
    }
  } else {
    if ($(input).val().trim() == ''){
        return false;
    }
  }
};

var showValidate = function(input) {
  var thisAlert = $(input).parent();
  $(thisAlert).addClass('alert-validate');
};

var hideValidate = function(input) {
  var thisAlert = $(input).parent();
  $(thisAlert).removeClass('alert-validate');
};

var checkInput = function() {
  var check = true;
  $.each($('.validate-input .input100'), function(i, ipt) {
    if (validate(ipt) == false) {
      showValidate(ipt);
      check = false;
    }
  });

  return check;
};

var loginUser = function() {
  var uid = $('#uemail').val();
  var pwd = $('#upassword').val();
  $.ajax({
    url: '/painter/login',
    method: 'POST',
    data: {
      uid: uid,
      pwd: pwd
    },
    success: function(data) {
      window.location.replace('/index.html');
    },
    error: function(jqr, status, err) {
      if (401 === jqr.status) {
        alert('Login failed');
      } else if (400 === jqr.status) {
        alert('Input invalid');
      } else {
        alert(jqr.statusText);
      }
    }
  });
};

$(function() {
  $('.validate-form .input100').each(function(){
    $(this).focus(function() {
      hideValidate(this);
    });
  });

  $('#loginBtn').on('click', function() {
    if (checkInput()) {
      loginUser();
    }
  });
});