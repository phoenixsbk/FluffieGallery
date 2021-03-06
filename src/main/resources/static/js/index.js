var getLatestWeek = function() {
  $.ajax({
    url: '/week/latest',
    dataType: 'json',
    success: function(data) {
      if (data) {
        var weekname = data.name;
        if (data.paintings && data.paintings.length > 0) {
          $.each(data.paintings, function(i, v) {
            var block = createBlock(weekname, v);
            $('#homepagecontainer').append(block);
          });
        }
      }
    },
    error: function(err) {
    }
  });
};

var createBlock = function(weekname, painting) {
  var paddiv = $('<div>', {
    'class': 'col-12 col-md-3 col-lg-2 no-padding'
  });

  var contentdiv = $('<div>', {
    'class': 'portfolio-content'
  });
  paddiv.append(contentdiv);

  var figure = $('<figure>', {
    'class': 'img-auto'
  });
  var figimg = $('<img>', {
    'src': '/gallery/week_' + weekname + '/' + painting.filePath
  });
  figure.append(figimg);
  contentdiv.append(figure);

  var linkdiv = $('<div>', {
    'class': 'entry-content flex flex-column align-items-center justify-content-center'
  });
  contentdiv.append(linkdiv);

  var hline = $('<h4>');
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
    'href': '/painter.html?id=' + painting.painter.id
  });
  liline.append(lia);
  lia.append(painting.painter.name);

  ulline.append(liline);
  linkdiv.append(ulline);

  return paddiv;
}

$(function() {
  getLatestWeek();
})