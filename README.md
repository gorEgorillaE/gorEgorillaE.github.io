<!DOCTYPE html>
<html lang="sv">
<head>
<meta charset="utf-8">
<title>SafeVery</title>
<script src="https://unpkg.com/quagga/dist/quagga.min.js"></script>
<style>
body { font-family: Arial, sans-serif; padding: 20px; }
.hidden { display: none; }
button { padding: 12px; font-size: 16px; margin: 5px; }
input { padding: 10px; font-size: 16px; margin: 5px; width: 90%; }
#scanner { width: 100%; height: 300px; border: 1px solid #ccc; }
</style>
</head>
<body>

<h2 id="title">Skanna streckkod</h2>

<div id="scanner"></div>

<div id="manualCode" class="hidden">
  <p>Skriv in specialkod:</p>
  <input id="manualInput" placeholder="safevery">
  <button onclick="checkManual()">OK</button>
</div>

<div id="choice" class="hidden">
  <button onclick="chooseRole('fa')">FÅ</button>
  <button onclick="chooseRole('skicka')">SKICKA</button>
</div>

<div id="form" class="hidden">
  <p>Ditt namn:</p>
  <input id="name">
  <p>Telefonnummer:</p>
  <input id="phone">
  <button onclick="submitForm()">Fortsätt</button>
</div>

<div id="nfc" class="hidden">
  <p id="nfcText"></p>
  <button onclick="writeNFC()">Skriv till NFC</button>
  <button onclick="readNFC()">Läs NFC</button>
</div>

<script>
const BARCODE = "safevery148163267";
const QRCODE = "safevery148163267123465465sdadyguhasjhmvsdgfgffgdffgfuysyduafgda";
const MANUAL = "safevery";

let step = 1;
let role = "";
let myData = "";

window.onload = () => startBarcode();

function startBarcode() {
  Quagga.init({
    inputStream: {
      type: "LiveStream",
      target: document.querySelector("#scanner"),
      constraints: { facingMode: "environment" }
    },
    decoder: { readers: ["code_128_reader", "ean_reader"] }
  }, err => {
    if (err) return alert("Kamera fel");
    Quagga.start();
  });

  Quagga.onDetected(res => {
    const code = res.codeResult.code;
    if (step === 1 && code === BARCODE) {
      step = 2;
      Quagga.stop();
      startQR();
    }
  });
}

function startQR() {
  document.getElementById("title").innerText = "Skanna QR-kod";
  const detector = new BarcodeDetector({ formats: ["qr_code"] });
  const video = document.createElement("video");
  video.setAttribute("autoplay", "");
  document.getElementById("scanner").innerHTML = "";
  document.getElementById("scanner").appendChild(video);

  navigator.mediaDevices.getUserMedia({ video: { facingMode: "environment" } })
    .then(stream => {
      video.srcObject = stream;
      setInterval(async () => {
        const codes = await detector.detect(video);
        if (codes.length && codes[0].rawValue === QRCODE) {
          stream.getTracks().forEach(t => t.stop());
          document.getElementById("scanner").classList.add("hidden");
          document.getElementById("manualCode").classList.remove("hidden");
        }
      }, 1000);
    });
}

function checkManual() {
  if (document.getElementById("manualInput").value === MANUAL) {
    document.getElementById("manualCode").classList.add("hidden");
    document.getElementById("choice").classList.remove("hidden");
  } else {
    alert("Fel kod");
  }
}

function chooseRole(r) {
  role = r;
  document.getElementById("choice").classList.add("hidden");
  document.getElementById("form").classList.remove("hidden");
}

function submitForm() {
  const name = document.getElementById("name").value;
  const phone = document.getElementById("phone").value;
  myData = `BEGIN:VCARD
VERSION:3.0
FN:${name}
TEL:${phone}
END:VCARD`;

  document.getElementById("form").classList.add("hidden");
  document.getElementById("nfc").classList.remove("hidden");
  document.getElementById("nfcText").innerText =
    role === "skicka"
      ? "Skriv din kontakt till NFC"
      : "Läs kontakt från NFC";
}

async function writeNFC() {
  try {
    const writer = new NDEFWriter();
    await writer.write({
      records: [{ recordType: "mime", mediaType: "text/vcard", data: myData }]
    });
    alert("Kontakt skriven till NFC");
  } catch {
    alert("NFC-skrivning misslyckades");
  }
}

async function readNFC() {
  try {
    const reader = new NDEFReader();
    await reader.scan();
    reader.onreading = e => {
      const text = new TextDecoder().decode(e.message.records[0].data);
      alert("Kontakt mottagen:\n\n" + text);
    };
  } catch {
    alert("NFC-läsning misslyckades");
  }
}
</script>

</body>
</html>
