<!DOCTYPE html>
<html lang="sv">
<head>
  <meta charset="utf-8">
  <title>Streckkodsläsare</title>
  <script src="https://unpkg.com/quagga/dist/quagga.min.js"></script>
</head>
<body>

<h2>Skanna streckkod</h2>
<div id="scanner" style="width:100%; height:300px;"></div>

<script>
window.onload = function () {

  const SPECIAL_KOD = "safevery148163267"; // DIN KOD
  const REDIRECT_URL = "https://exempel.se"; // ÄNDRA HIT

  Quagga.init({
    inputStream: {
      name: "Live",
      type: "LiveStream",
      target: document.querySelector("#scanner"),
      constraints: {
        facingMode: "environment" // bakre kameran på mobil
      }
    },
    decoder: {
      readers: ["code_128_reader", "ean_reader", "ean_8_reader"]
    }
  }, function (err) {
    if (err) {
      console.error(err);
      alert("Kunde inte starta kameran");
      return;
    }
    Quagga.start();
  });

  Quagga.onDetected(function (result) {
    const code = result.codeResult.code;
    console.log("Hittad kod:", code);

    if (code === SPECIAL_KOD) {
      Quagga.stop(); // VIKTIGT
      window.location.href = REDIRECT_URL;
    }
  });

};
</script>

</body>
</html>
