/* ====== World map (Leaflet) ====== */
(function(){
    const mapEl = document.getElementById('mapCanvas');
    if(!mapEl || typeof L === 'undefined') return;
  
    // Carte
    const map = L.map(mapEl, {
      worldCopyJump: true,
      zoomControl: true,
      attributionControl: true,
      minZoom: 2
    }).setView([20, 0], 2);
  
    // Tuiles (Carto light)
    L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}.png', {
      attribution:
        '&copy; <a href="https://www.openstreetmap.org/copyright">OSM</a> &copy; <a href="https://carto.com/attributions">CARTO</a>'
    }).addTo(map);
  
    // Limites approximatives des continents (lat,lng)
    const CONTINENTS = {
      "Afrique":      [[-35,-20],[38,55]],
      "Amérique du Nord": [[7,-168],[83,-52]],
      "Amérique du Sud": [[-56,-82],[13,-34]],
      "Asie":         [[-1,26],[78,180]],
      "Europe":       [[35,-31],[72,45]],
      "Océanie":      [[-48,110],[-5,180]]
    };
  
    // Synchroniser les chips
    const chipsWrap = document.querySelector('.map-section .chips');
    const chips = chipsWrap ? Array.from(chipsWrap.querySelectorAll('.chip')) : [];
  
    function activateChip(name){
      chips.forEach(c => c.classList.toggle('is-active', c.textContent.trim()===name));
    }
    chips.forEach(chip=>{
      chip.addEventListener('click', ()=>{
        const name = chip.textContent.trim();
        const b = CONTINENTS[name];
        if(b){ map.fitBounds(b, { padding:[30,30] }); activateChip(name); }
      });
    });
  
    // Petit marker au clic
    let clickMarker;
    map.on('click', (e)=>{
      if(clickMarker) map.removeLayer(clickMarker);
      clickMarker = L.circleMarker(e.latlng, {
        radius:6, color:getComputedStyle(document.documentElement).getPropertyValue('--map-accent').trim() || '#F5951E',
        weight:2, fillOpacity:.6
      }).addTo(map).bindPopup(`Lat: ${e.latlng.lat.toFixed(2)}<br>Lng: ${e.latlng.lng.toFixed(2)}`).openPopup();
    });
  
    // Liens par continent (si tu veux ouvrir des pages en 1 clic)
    map.on('dblclick', ()=>{
      const active = chips.find(c => c.classList.contains('is-active'));
      if(!active) return;
      const name = active.textContent.trim();
      const routes = {
        "Afrique":"/Afrique",
        "Amérique du Nord":"/AmeriqueNord",
        "Amérique du Sud":"/AmeriqueSud",
        "Asie":"/Asie",
        "Europe":"/Europe",
        "Océanie":"/Oceanie"
      };
      const url = routes[name];
      if(url) window.location.href = url;
    });
  })();
  
  