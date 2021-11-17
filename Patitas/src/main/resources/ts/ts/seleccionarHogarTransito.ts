const seleccionarHogarTransitoMain = () => {
    fetchHogares();
}

//document.addEventListener('DOMContentLoaded', seleccionarHogarTransitoMain);
document.querySelector('#google-maps-script').addEventListener('load', seleccionarHogarTransitoMain);

// Esta funcion la llama el creador de mapas de google maps
function initMap(): void {
}

// Esta interfaz va a cambiar cuando implementemos nuestra API
interface Hogar {
    id: string;
    nombre: string;
    telefono: string;
    ubicacion: {
        direccion:string;
        lat: number;
        long: number;
    };
    admisiones: {
        perros: boolean;
        gatos: boolean;
    };
    capacidad: number;
    lugares_disponibles: number;
    caracteristicas: string[];
}

const _initCardMap = (ubicacion: { direccion:string; lat: number; long: number; }, mapa: HTMLElement) => {
    const pos = {
        lat: ubicacion.lat,
        lng: ubicacion.long
    };

    const map = new google.maps.Map(
        mapa, {
            zoom: 17,
            center: pos
        }
    );

    const marker = new google.maps.Marker({
        position: pos,
        map: map,
      });
}

const _createCard = (hogar: Hogar, n: number): HTMLElement => {
    let cardContainer = document.createElement('div');
    cardContainer.classList.add('card');
    cardContainer.classList.add('shadow-lg');
    cardContainer.classList.add('m-3');

    const mapID = `map${n}`;

    let map = document.createElement('div');
    map.classList.add('card-map');
    map.classList.add('card-img-top');
    map.id = mapID;
    cardContainer.appendChild(map);

    let cardBody = document.createElement('div');
    cardBody.classList.add('card-body');
    cardBody.classList.add('d-flex');
    cardBody.classList.add('flex-column');
    cardBody.classList.add('justify-content-between');
    cardContainer.appendChild(cardBody);

    let h5_title = document.createElement('h5');
    h5_title.innerHTML = hogar.nombre;
    h5_title.classList.add('card-title');
    cardBody.appendChild(h5_title);

    let p1 = document.createElement('p1');
    p1.classList.add('card-text');
    p1.innerHTML = hogar.ubicacion.direccion;
    cardBody.appendChild(p1);

    let p2 = document.createElement('p1');
    p2.classList.add('card-text');
    p2.innerHTML = hogar.telefono;
    cardBody.appendChild(p2);

    let btnDiv = document.createElement('div');
    let btn = document.createElement('button');
    btnDiv.appendChild(btn);
    btn.classList.add('btn');
    btn.classList.add('align-self-end');
    btn.classList.add('btn-primary');
    btn.innerHTML = 'Seleccionar';
    cardBody.appendChild(btnDiv);

    _initCardMap(hogar.ubicacion, map);

    return cardContainer;
}

const _displayHogares = (content:Hogar[]): void => {
    console.log(content);
    const cardContainer = document.getElementById('card-container') as HTMLElement;
    for(let i = 0; i < content.length; i++) {
        let card = _createCard(content[i], i);
        cardContainer.appendChild(card);
    }
}

/*
    No usamos fetch API porque este metodo tiene mas compatibilidad

    Por ahora hacemos fetch a la API, pero a futuro va a ser
    hacia nuestra API rest
*/

const _fetchXMLHttpRequest = () => {
    let http_req: XMLHttpRequest;
    if(window.XMLHttpRequest)
        http_req = new XMLHttpRequest();
    else // Para navegadores viejos
        http_req = new ActiveXObject('Microsoft.XMLHTTP');

    const _loadData = () => {
        if(http_req.readyState === 4) {
            if(http_req.status === 200)
                _displayHogares(JSON.parse(http_req.responseText).hogares);
            else
                console.error('Error solicitando los hogares');
        }
    }

    http_req.onreadystatechange = _loadData;
    http_req.open('GET', 'https://api.refugiosdds.com.ar/api/hogares?offset=1', true);
    http_req.setRequestHeader('accept', 'application/json');
    http_req.setRequestHeader('Authorization', "Bearer yv7FiiDALkWwgHSiqqu4rHMCwYiwNrkB5dzzxrlKH6QQJBlaHue8Fw2cTHiJ");
    http_req.send();
}

const _fetchAPI = () => {
    let link = 'https://api.refugiosdds.com.ar/api/hogares?offset=1';
    fetch(link, {
        method: 'GET',
        headers: new Headers({
            'Authorization': 'Bearer yv7FiiDALkWwgHSiqqu4rHMCwYiwNrkB5dzzxrlKH6QQJBlaHue8Fw2cTHiJ'
        })
    })
    .then(res => res.json())
    .then(data => _displayHogares(data.hogares))
    .catch(e => {
        console.error('Error solicitando los hogares con Fetch API');
        console.log(e);
    });
}

const _fetchTestData = () => {
    _displayHogares(testData);
}

const fetchHogares = () => {
    //_fetchXMLHttpRequest();
    //_fetchAPI();
    _fetchTestData();
}

const testData = [
    {
        "id": "eyJpdiI6IkNGQmlyR1kyMmlVbCtueHlpMXhDY1E9PSIsInZhbHVlIjoiam9rUng0emU4eXVhUjM3SVJpeG51QT09IiwibWFjIjoiODhiNjQ4NTY4YmMxNWQwMDM1OTJlYWExYTU5Zjc1ZGI1OWFkZGNiOWNmZjhhOThkMjEyNmEyNjBmNmQ3NGNlYyJ9",
        "nombre": "Pensionado de mascotas \"Como en casa\"",
        "ubicacion": {
            "direccion": "Av. Ing Eduardo Madero 2300, B1669BZQ Del Viso, Provincia de Buenos Aires",
            "lat": -34.46013439745161,
            "long": -58.80857841888721
        },
        "telefono": "+541164657462",
        "admisiones": {
            "perros": false,
            "gatos": true
        },
        "capacidad": 50,
        "lugares_disponibles": 45,
        "patio": true,
        "caracteristicas": [
            "Tranquilo",
            "Pacífico"
        ]
    },
    {
        "id": "eyJpdiI6IjB5aGJQdnJuQTlOUTVNczloZjVIUFE9PSIsInZhbHVlIjoiT0liS1AzVzN3blVTV1NIVFZMSXAzUT09IiwibWFjIjoiN2RjZDk2ZTBmMWRlYzEwMGM5NjBhMWIwYmNlOWE3MmNjMWVjMjgyZDM3NDJiMGI0MzllZDdhMjRmYTA1MDVlMCJ9",
        "nombre": "Adopteros Argentina",
        "ubicacion": {
            "direccion": "Plaza Teniente General Emilio Mitre, Recoleta, Buenos Aires",
            "lat": -34.58805543938273,
            "long": -58.39709555890073
        },
        "telefono": "+541140893717",
        "admisiones": {
            "perros": true,
            "gatos": false
        },
        "capacidad": 50,
        "lugares_disponibles": 20,
        "patio": true,
        "caracteristicas": []
    },
    {
        "id": "eyJpdiI6IjBOeUI0NjJuMWpYRWYxRGVhN1NBdkE9PSIsInZhbHVlIjoiQ3VuSjdUczdjNmRNNkc3V2l3TVFiQT09IiwibWFjIjoiMzFlNzdlYTAxZDM4MTIxZTE1ODBmNGYwYTE2NTY3ZWY3OWQ4YTNkMmJhNTVmODBhMGI1Njk4NGY5YzJhZDZhNyJ9",
        "nombre": "Las Renatas",
        "ubicacion": {
            "direccion": "Belén de Escobar, Provincia de Buenos Aires",
            "lat": -34.321757318424986,
            "long": -58.76384353218433
        },
        "telefono": "+541130017001",
        "admisiones": {
            "perros": true,
            "gatos": true
        },
        "capacidad": 150,
        "lugares_disponibles": 89,
        "patio": true,
        "caracteristicas": []
    },
    {
        "id": "eyJpdiI6IjhsdGZlQSt0SzQ3NUtVVkl3WEtFaEE9PSIsInZhbHVlIjoidkl0TVUxZTFsNDA0bk04WDdJT1R3dz09IiwibWFjIjoiN2JkYmRhNmYxODU2MDFmNDBjNDBlYmU5NzQyNTM0NmNhZWFhOWFiOGUyOTM4ZTQ0NGFjMzA4ODI3ZmFlYjhkZiJ9",
        "nombre": "Ayudacan",
        "ubicacion": {
            "direccion": "Jerónimo Salguero 151,C1177AEA, C1177 AEA, Buenos Aires",
            "lat": -34.60909997098105,
            "long": -58.419918482852644
        },
        "telefono": "+541134586100",
        "admisiones": {
            "perros": true,
            "gatos": false
        },
        "capacidad": 150,
        "lugares_disponibles": 49,
        "patio": true,
        "caracteristicas": []
    },
    {
        "id": "eyJpdiI6ImZ3bDk5aHovR0s3RmREbzYrdnZCN3c9PSIsInZhbHVlIjoib1hnZk1nMFdIL1RyOWxVOGk1SERQUT09IiwibWFjIjoiZGVjOWE4ZjUyZWEwZjliMzFjMTRkZTliYjA4MjBjNzFkMjE0YzRlZWM2ZDlmZTA2M2YwZmRiZmExYmNjYWZlOSJ9",
        "nombre": "El campito Refugio",
        "ubicacion": {
            "direccion": "ruta 16 Parc 501, B1842 Monte Grande, Provincia de Buenos Aires",
            "lat": -34.891156274674614,
            "long": -58.43278121166188
        },
        "telefono": "+541123965480",
        "admisiones": {
            "perros": true,
            "gatos": true
        },
        "capacidad": 180,
        "lugares_disponibles": 80,
        "patio": true,
        "caracteristicas": []
    },
    {
        "id": "eyJpdiI6InVWeFpIWG1ldWZVakQ3ZWViVmRSbGc9PSIsInZhbHVlIjoiZkdLNTczN3M2TndnZWorZXZtZnZEQT09IiwibWFjIjoiZTEwNWYxM2EzMGFjYTU0OTY3OWI1OTFlMWU0NTY2ZTJjYTExYjI5OWJlNDllYTA0YzliZjI3OGM4OWM1ZDg1MSJ9",
        "nombre": "Casita de Adopciones en Tigre",
        "ubicacion": {
            "direccion": "Los Mimbres 162, B1648 DUB, Provincia de Buenos Aires",
            "lat": -34.42061525423029,
            "long": -58.572775488348505
        },
        "telefono": "+541144096338",
        "admisiones": {
            "perros": true,
            "gatos": true
        },
        "capacidad": 200,
        "lugares_disponibles": 110,
        "patio": true,
        "caracteristicas": []
    },
    {
        "id": "eyJpdiI6Im11UUYwQ0VuYkh5UUFQV0U2eVBQb1E9PSIsInZhbHVlIjoiRzFqYU0vVzlBU1dBS2JOazR6Y095UT09IiwibWFjIjoiYjM2MDgxNThkMDE3MDJmODI2NTQ4MzAyN2ViOThkZjU4NTAxOGY0YTM4YWMxOTE4MWFkNGE2ZWE4OWZjMmU3NCJ9",
        "nombre": "Adopciones Quilmes",
        "ubicacion": {
            "direccion": "Autopista Buenos Aires - La Plata, Quilmes, Provincia de Buenos Aires",
            "lat": -34.72110595257815,
            "long": -58.25543198834849
        },
        "telefono": "+541169233782",
        "admisiones": {
            "perros": true,
            "gatos": false
        },
        "capacidad": 20,
        "lugares_disponibles": 1,
        "patio": false,
        "caracteristicas": [
            "Manso"
        ]
    },
    {
        "id": "eyJpdiI6IjloN2JWMXRmcVM3aVNhRFJIcnY1NWc9PSIsInZhbHVlIjoiamxLOHJWb1o2RTRxbnRpRG1ZS3czUT09IiwibWFjIjoiOTExNDU0YTU4OTRjNzRkZDY5MDdkYWFhNDNiNmQzNDJkOTkyNDM0NDFhMDAwYTZiMTJhMTVmMjNlOWQxZjQ5NCJ9",
        "nombre": "A.P.R.E.",
        "ubicacion": {
            "direccion": "Periodista Augusto Prieto, Lanús, Provincia de Buenos Aires",
            "lat": -34.69597964404415,
            "long": -58.38562581166187
        },
        "telefono": "+541144348089",
        "admisiones": {
            "perros": true,
            "gatos": true
        },
        "capacidad": 120,
        "lugares_disponibles": 30,
        "patio": true,
        "caracteristicas": []
    },
    {
        "id": "eyJpdiI6IkhHcDU0N2NScVZuRFlhTVQxYmhhZEE9PSIsInZhbHVlIjoiTEhubGVOSU5SblE1eU1KZ2N1ZmIxUT09IiwibWFjIjoiYjAzMTM0MjQ1MzVkYjRiY2NmMWFmYzNlNDgwMDA3ODY1YmE0YjQyOWMzZjEyM2IyZWY0MGViNjk0MjM5ZTlmOCJ9",
        "nombre": "REFUGIO PARA PERROS SARANDI",
        "ubicacion": {
            "direccion": "Nicaragua 2020, B1872 Sarandí, Provincia de Buenos Aires",
            "lat": -34.67901049853944,
            "long": -58.338400317796285
        },
        "telefono": "+541138516102",
        "admisiones": {
            "perros": true,
            "gatos": false
        },
        "capacidad": 30,
        "lugares_disponibles": 5,
        "patio": true,
        "caracteristicas": []
    },
    {
        "id": "eyJpdiI6ImJ4V1E4R0FDazN1R3ljRWpsdS93OFE9PSIsInZhbHVlIjoiNWtGK2RYN2dsOVdjV1puaTJGenVjZz09IiwibWFjIjoiOTE0MTA5MWMyODQyNTk2YTRiNjk2YTVhNDg0MTgxZjdmMTQ3ODUzYzBlYTA2ZDQ4MTA2OWQ5ZGFhNGY3ZDkxNyJ9",
        "nombre": "Hogar de Proteccion Lourdes",
        "ubicacion": {
            "direccion": "C1098ABA, Chile 1393, C1098 Buenos Aires",
            "lat": -34.61657666535632,
            "long": -58.38580212945297
        },
        "telefono": "+541151835168",
        "admisiones": {
            "perros": true,
            "gatos": true
        },
        "capacidad": 22,
        "lugares_disponibles": 3,
        "patio": true,
        "caracteristicas": [
            "Delgado",
            "Amistoso",
            "Manso"
        ]
    }
];
