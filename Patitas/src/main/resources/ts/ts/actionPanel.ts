let responsesGeneralDiv: HTMLDivElement;
let responsesAsociacionDiv: HTMLDivElement;
let formGeneral: HTMLFormElement;
let formVoluntario: HTMLFormElement;

const actionPanelMain = () => {
   responsesGeneralDiv = document.querySelector("#responsesGeneral");
   responsesAsociacionDiv = document.querySelector("#responsesAsociacion");
   formGeneral = document.querySelector("#admin-form");
   formVoluntario = document.querySelector("#voluntario-form");
   document.querySelector('#posibilidad-general-btn')?.addEventListener('click', addPosibilidadGeneral);
   document.querySelector('#posibilidad-voluntario-btn')?.addEventListener('click', addPosibilidadVoluntario);
   formGeneral?.querySelector("input[type=submit]")?.addEventListener('click', submitPosibilidadGeneral);
   formVoluntario?.querySelector("input[type=submit]")?.addEventListener('click', submitPosibilidadVoluntario);
}

document.addEventListener('DOMContentLoaded', actionPanelMain);

const removerDiv = (container: HTMLDivElement, e: HTMLDivElement) => {
    container.removeChild(e);
}

const makeDiv = (container: HTMLDivElement) => {
    let div = document.createElement('div');
    div.classList.add('mb-3');
    div.classList.add('d-flex');
    div.innerHTML = `
    <div class='form-floating'>
        <input name="rta" type="text" class="form-control" id="floatingInput" placeholder="Respuesta" />
        <label for="floatingInput">Respuesta</label>
    </div>
    `
    let btn = document.createElement('a');
    btn.onclick = () => removerDiv(container, div);
    btn.innerHTML = 'X';
    btn.classList.add('btn');
    btn.classList.add('btn-danger');
    btn.classList.add('mx-2');
    let div2 = document.createElement('div');
    div2.classList.add('d-flex');
    div2.classList.add('justify-content-center');
    div2.classList.add('align-items-center');
    div2.appendChild(btn);
    div.appendChild(div2);
    return div;
}

const addPosibilidadGeneral = (e: Event) => {
    e.preventDefault();
    responsesGeneralDiv.appendChild(makeDiv(responsesGeneralDiv));
}

const addPosibilidadVoluntario = (e: Event) => {
    e.preventDefault();
    responsesAsociacionDiv.appendChild(makeDiv(responsesAsociacionDiv));
}

const submitPosibilidadGeneral = (e : Event) => {
    e.preventDefault();
    let els = formGeneral.querySelectorAll('input[name=rta]') as NodeListOf<HTMLInputElement>;
    for(let i = 0; i < els.length; i++)
        els[i].name += i;

    formGeneral.submit();
}

const submitPosibilidadVoluntario = (e : Event) => {
    e.preventDefault();
    let els = formVoluntario.querySelectorAll('input[name=rta]') as NodeListOf<HTMLInputElement>;
    for(let i = 0; i < els.length; i++)
        els[i].name += i;

    formVoluntario.submit();
}