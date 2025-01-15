$('#attach').on('change', function(event) {
    const selectedFile = event.target.files[0];
    if (selectedFile) {
      const fileReader = new FileReader();
      fileReader.onload = function(e) {
        $('#selected-image').attr('src', e.target.result);
        // $('#selected-image')
      };
      fileReader.readAsDataURL(selectedFile);
    }
})