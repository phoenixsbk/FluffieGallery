var getLatestWeek = function() {
  $.ajax({
    url: '/week/latest',
    dataType: 'json',
    success: function(data) {
      if (data) {
        var weekid = data.id;
        if (data.paintings && data.paintings.length > 0) {
          $.each(data.paintings, function(i, v) {
            console.log(v);
            var block = createBlock(weekid, v.name, '', v.filePath);
            $('#homepagecontainer').append(block);
          });
        }
      }
    },
    error: function(err) {
    }
  });
};

var createBlock = function(weekid, name, author, filepath) {
  var paddiv = $('<div>', {
    'class': 'col-12 col-md-6 col-lg-3 no-padding'
  });

  var contentdiv = $('<div>', {
    'class': 'portfolio-content'
  });
  paddiv.append(contentdiv);

  var figure = $('<figure>');
  var figimg = $('<img>', {
    'src': '/gallery/week_' + weekid + '/' + filepath
  });
  figure.append(figimg);
  contentdiv.append(figure);

  var linkdiv = $('<div>', {
    'class': 'entry-content flex flex-column align-items-center justify-content-center'
  });
  contentdiv.append(linkdiv);

  var h3line = $('<h3>');
  var linkline = $('<a>', {
    'href': '#'
  });
  linkline.append(name);
  h3line.append(linkline);
  linkdiv.append(h3line);

  var ulline = $('<ul>', {
    'class': 'flex flex-wrap justify-content-center'
  });

  var liline = $('<li>');
  var lia = $('<a>', {
    'href': '#'
  });
  liline.append(lia);
  lia.append(name);

  var lilineb = $('<li>');
  var lib = $('<a>', {
    'href': '#'
  });
  lilineb.append(lib);
  ulline.append(liline);
  ulline.append(lilineb);
  linkdiv.append(ulline);

  return paddiv;
}

$(function() {
  getLatestWeek();
})