var getAllPainters = function() {
  $.ajax({
    url: '/painter/all',
    dataType: 'json',
    success: function(data) {
      if (data && data.length > 0) {
        $.each(data, function(i, p) {
          $('#paintercontainer').append(createBlock(p));
        });
      }
    },
    error: function(err) {
    }
  });
};

var createBlock = function(painter) {
  var paddiv = $('<div>', {
    'class': 'col-12 col-md-4 col-lg-3'
  });

  var contentdiv = $('<div>', {
    'class': 'portfolio-content'
  });
  paddiv.append(contentdiv);

  var figure = $('<figure>', {
    'class': 'img-sm-auto'
  });
  var figimg = $('<img>', {
    'src': '/avatar/' + painter.avatar,
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
    'href': '/painter.html?id=' + painter.id
  });
  linkline.append(painter.name);
  hline.append(linkline);
  linkdiv.append(hline);

  var ulline = $('<ul>', {
    'class': 'flex flex-wrap justify-content-center'
  });

  /*var liline = $('<li>');
  var lia = $('<a>', {
    'href': '/painter.html?id=' + painter.id
  });
  liline.append(lia);
  lia.append(painter.name);

  ulline.append(liline);*/
  linkdiv.append(ulline);

  return paddiv;
}

$(function() {
  getAllPainters();
})