document.addEventListener('DOMContentLoaded', () => {
   let elems = document.querySelectorAll('.card-body > p:first-of-type') as NodeListOf<HTMLParagraphElement>;

   // Hacemos mayuscula la primer letra nada mas
    for(let e of elems)
        e.innerHTML = e.innerHTML.charAt(0) + e.innerHTML.slice(1).toLowerCase();
});

const warningDialog = (id : number) => {
    const str = `
    <div class="p-4 w-25">
        <h4>¿Estas seguro que deseas realizar esta accion?</h4>
        <hr />
        <div>
            <button class="btn btn-primary">Confirmar</button>
            <button class="btn btn-secondary">Cancelar</button>
        </div>
    </div>
    `;
    let div = document.createElement('div');
    div.classList.add('page-dialog');
    div.innerHTML = str;

    div.querySelector('button.btn-primary').addEventListener('click', () => _submitFormMascota(id));
    div.querySelector('button.btn-secondary').addEventListener('click', () => _removeDialog(div));
    document.querySelector('body').appendChild(div);
}

const _removeDialog = (div : HTMLDivElement) => {
    // Seleccionamos el container de la pagina
    document.querySelector('body').removeChild(div);
}

const _submitFormMascota = (id : number) => {
    (document.getElementById(`${id}`) as HTMLFormElement).submit();
}