var getPainting = function(id) {
  $.ajax({
    url: '/painting/paintingid/' + id,
    dataType: 'json',
    success: function(data) {
      var weekname = data.week.name;
      var filepath = data.filePath;
      $('#paintingimg').attr('src', '/gallery/week_' + weekname + '/' + filepath);
      $('#paintingdesc').text(data.description);
      $('#crumbweek').text(weekname);
      $('#crumbweek').attr('href', '/week.html?id=' + data.week.id);
      $('#weekLink').text(weekname);
      $('#weekLink').attr('href', '/week.html?id=' + data.week.id);
      $('#painterLink').text(data.painter.name);
      $('#painterLink').attr('href', '/painter.html?id=' + data.painter.id);

      if (data.comments && data.comments.length > 0) {
        $.each(data.comments, function(i, c) {
          $('#commentscontainer').append(createComment(c))
        });
      }
    },
    error: function(err) {
    }
  });
};

var createComment = function(comment) {
  var licontainer = $('<li>', {
    'class': 'comment'
  });

  var article = $('<articl>', {
    'class': 'comment-body'
  });
  licontainer.append(article);

  var fig = $('<figure>', {
    'class': 'comment-author-avatar'
  });
  article.append(fig);

  var figimg = $('<img>', {
    'src': '/avatar/' + comment.painter.avatar,
    'alt': comment.painter.name
  });
  fig.append(figimg);

  var wrap = $('<div>', {
    'class': 'comment-wrap'
   });
   article.append(wrap);

   var authorline = $('<div>', {
    'class': 'comment-author'
   });
   wrap.append(authorline);

   var spanline = $('<span>', {
    'class': 'fn'
   });
   authorline.append(spanline);
   spanline.append(comment.painter.name);

   var dateline = $('<span>', {
    'class': 'comment-meta'
   });
   authorline.append(dateline);
   dateline.append(comment.date);

   var pline = $('<p>');
   pline.append(comment.text);
   wrap.append(pline);

   return licontainer;
};

$(function() {
  var urlParams = new URLSearchParams(window.location.search);
  if (urlParams) {
    var paintingid = urlParams.get('id');
    if (paintingid) {
      getPainting(paintingid);
    }
  }
})