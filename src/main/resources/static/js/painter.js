var getPainter = function(id) {
  $.ajax({
    url: '/painter/painterid/' + id,
    dataType: 'json',
    success: function(data) {
      $('#painteravatar').attr('src', '/avatar/' + data.avatar);
      $('#paintername').text(data.name);
      $('#crumbpainter').text(data.name);
      $('#painterdesc').text(data.description);
      if (data.artifacts && data.artifacts.length > 0) {
        $.each(data.artifacts, function(i , p) {
          $('#painterartifactscontainer').append(createBlock(data.name, p));
        });
      }
    }
  });
};

var createBlock = function(paintername, painting) {
  var paddiv = $('<div>', {
    'class': 'col-12 col-md-3 col-lg-2'
  });

  var contentdiv = $('<div>', {
    'class': 'portfolio-content'
  });
  paddiv.append(contentdiv);

  var figure = $('<figure>', {
    'class': 'img-sm-auto'
  });
  var figimg = $('<img>', {
    'src': '/gallery/week_' + painting.week.name + '/' + painting.filePath,
    'class': 'img-center'
  });
  figure.append(figimg);
  contentdiv.append(figure);

  var linkdiv = $('<div>', {
    'class': 'entry-content flex flex-column align-items-center justify-content-center'
  });
  contentdiv.append(linkdiv);

  var hline = $('<h5>');
  var linkline = $('<a>', {
    'href': '/painting.html?id=' + painting.id
  });
  linkline.append(painting.name);
  hline.append(linkline);
  linkdiv.append(hline);

  var ulline = $('<ul>', {
    'class': 'flex flex-wrap justify-content-center'
  });

  var liline = $('<li>');
  var lia = $('<a>', {
    'href': '/painting.html?id=' + painting.id
  });
  liline.append(lia);
  lia.append(paintername);

  ulline.append(liline);
  linkdiv.append(ulline);

  return paddiv;
}

$(function() {
  var urlParams = new URLSearchParams(window.location.search);
  if (urlParams) {
    var painterid = urlParams.get('id');
    if (painterid) {
      getPainter(painterid);
    }
  }
})