interface LocationInterface {
    lat: number;
    lng: number;
}

let contactMethodContainer:HTMLDivElement;

const defaultFormMain = () => {
    // Para los otros input
    document.querySelector('input[type="submit"]:not(.ignore-default-form)').addEventListener('click', submitForm);
    document.querySelector('#add-contact')?.addEventListener('click', addContactMethod);
    document.querySelector('#add-contact-bootstrap')?.addEventListener('click', addContactMethodBootstrap);

    // Agregamos el primer input
    contactMethodContainer = document.querySelector("#contact-section-bootstrap");

    // Si este es el caso, tenemos un input que primero no tiene ningun metodo de contacto
    if(contactMethodContainer) {
        addContactMethodBootstrap();
        let firstContact = document.querySelector('.contact-card-container');
        // Removemos el hr y el boton para eliminar el input ya que esta es obligatoria
        let children = firstContact.children;
        firstContact.removeChild(children[children.length - 1])
        firstContact.removeChild(children[0]);
    } else {
        contactMethodContainer = document.querySelector("#modify-contact-section-bootstrap");
        bindMethods();
    }
}

document.addEventListener('DOMContentLoaded', defaultFormMain);

const _createInputType = (type: string, name: string) => {
    let elem = document.createElement('input');
    elem.type = type;
    elem.name = name;
    elem.placeholder = name;

    if (type === 'tel')
        elem.pattern = '11-[0-9]{4}-[0-9]{4}';

    return elem;
}

const removeContactMethod = (div: HTMLElement) => {
    let contact_section = document.querySelector("#contact-section");
    contact_section.removeChild(div);
}

const addContactMethod = () => {
    let contact_section = document.querySelector("#contact-section");
    let div = document.createElement('div');
    div.appendChild(document.createElement('hr'));
    div.appendChild(_createInputType('text', 'Nombre'));
    div.appendChild(_createInputType('text', 'Apellido'));
    div.appendChild(_createInputType('tel', 'Telefono'));
    div.appendChild(_createInputType('email', 'Email'))

    let btnDiv = document.createElement('div');
    btnDiv.classList.add('contact-remove-container');

    let btn = document.createElement('button');
    btn.type = 'button';
    btn.onclick = () => removeContactMethod(div);
    btn.classList.add('contact-remove-btn');
    btn.innerHTML = 'Eliminar';

    btnDiv.appendChild(btn);
    div.appendChild(btnDiv);

    contact_section.appendChild(div);
}

/*

    For bootstrap

*/

let contactDivs : HTMLDivElement[] = [];

const removeContactMethodBootstrap = (div: HTMLDivElement) => {
    contactMethodContainer.removeChild(div);

    let idx = contactDivs.indexOf(div);
    if(idx > -1)
        contactDivs.splice(idx, 1);
}

const enableDisableDecoy = (e : HTMLInputElement) => {
    e.disabled = !e.disabled;
}

const addContactMethodBootstrap = () => {
    let div = document.createElement('div');
    'd-flex w-100 flex-wrap alt-form-container justify-content-between contact-card-container'.split(" ").forEach(c => div.classList.add(c));

    div.innerHTML = `
        <hr class='my-3' style="width: 95%; height: 2px; background: black;" />
        <div class="form-floating mb-3">
            <input type="text" name="nombreContacto" class="form-control" id="floatingName"
                placeholder="Nombre" required>
            <label for="floatingName">Nombre</label>
        </div>
        <div class="form-floating mb-3">
            <input type="text" name="apellidoContacto" class="form-control" id="floatingName"
                placeholder="Apellido" required>
            <label for="floatingName">Apellido</label>
        </div>
        <div class="form-floating mb-3">
            <input type="tel" name="telefonoContacto" class="form-control" id="floatingName"
                placeholder="Telefono" required>
            <label for="floatingName">Telefono</label>
        </div>
        <div class="form-floating mb-3">
            <input type="email" name="emailContacto" class="form-control" id="floatingName"
                placeholder="Correo electronico" required>
            <label for="floatingName">Correo electronico</label>
        </div>
        <div class='d-flex mb-3'>
            <div>
                <div class="form-check">
                    <input class="form-check-input wp-check" name="WhatsApp" type="checkbox" id="contactCheckbox">
                    <input class="form-check-input wp-decoy" style="display: none" name="WhatsApp" type="text" value="off">
                    <label class="form-check-label" for="contactCheckbox">
                        WhatsApp
                    </label>
                </div>
            </div>
            <div>
                <div class="form-check">
                    <input class="form-check-input sms-check" name="SMS" type="checkbox" id="contactCheckbox">
                    <input class="form-check-input sms-decoy" style="display: none" name="SMS" type="text" value="off">
                    <label class="form-check-label" for="contactCheckbox">
                        SMS
                    </label>
                </div>
            </div>
            <div>
                <div class="form-check">
                    <input class="form-check-input email-check" name="Email" type="checkbox" id="contactCheckbox">
                    <input class="form-check-input email-decoy" style="display: none" name="Email" type="text" value="off">
                    <label class="form-check-label" for="contactCheckbox">
                        Email
                    </label>
                </div>
            </div>
        </div>
        <div>
            <button type='button' class='contact-remove-btn btn btn-danger'>Eliminar</button>
        </div>
    `;
    bindContactMethod(div);
    contactMethodContainer.appendChild(div);
}

const bindMethods = () => {
    let divs = document.querySelectorAll( `#${contactMethodContainer.id} > div`) as NodeListOf<HTMLDivElement>;
    console.log(divs);

    for(let div of divs)
        bindContactMethod(div);
}

const bindContactMethod = (div: HTMLDivElement) => {
    let btn: HTMLButtonElement = div.querySelector('.contact-remove-btn');
    btn.onclick = () => removeContactMethodBootstrap(div);

    let elems:string[] = [ 'wp', 'sms', 'email' ];

    for(let str of elems) {
        let check = div.querySelector(`.${str}-check`);
        let decoy : HTMLInputElement = div.querySelector(`.${str}-decoy`);
        check.addEventListener('click', () => enableDisableDecoy(decoy));
    }
    contactDivs.push(div);
}

const submitForm = (e : Event) => {
    e.preventDefault();
    let names = ['nombreContacto', 'apellidoContacto', 'telefonoContacto', 'emailContacto', 'WhatsApp', 'SMS', 'Email'];

    let i = 0;
    for(let div of contactDivs) {
        // Nos fijamos si ese contact method tiene ID (ya existe)
        let idSelector = div.querySelector("input[name=idContactCard]") as HTMLInputElement;

        if(idSelector)
            idSelector.name = `idContactCard${i}`;
        for(let str of names) {
            let els = div.querySelectorAll(`input[name=${str}]`) as NodeListOf<HTMLInputElement>;

            for(let e of els)
                e.setAttribute("name", `${str}${i}`);
        }
        i++;
    }

    // Intentamos cargar la informacion del mapa
    let hayMapa = true;
    // @ts-ignore
    try { locationPicker; }
    catch (err) {
        hayMapa = false;
    }

    if(hayMapa) {
        // @ts-ignore
        let location: LocationInterface = locationPicker.getMarkerPosition();

        (document.querySelector("input[name='lat']") as HTMLInputElement).value = location.lat + '';
        (document.querySelector("input[name='lng']") as HTMLInputElement).value = location.lng + '';
    }

    (document.querySelector("form:not(.ignore-default-form)") as HTMLFormElement).submit();
}