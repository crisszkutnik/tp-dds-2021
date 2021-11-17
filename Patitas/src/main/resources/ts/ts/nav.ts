document.addEventListener('DOMContentLoaded', () => {
  let body = document.querySelector('body');

  const _nav = `
  <nav class="navbar navbar-expand-lg navbar-dark bg-primary bg-gradient">
          <div class="container-fluid">
              <a class="navbar-brand" href="/">
                  <img src="/img/paws.svg" alt="" width="30" height="24" class="d-inline-block align-text-top">
                  Patitas al rescate
              </a>
              <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                  data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false"
                  aria-label="Toggle navigation">
                  <span class="navbar-toggler-icon"></span>
              </button>
              <div class="collapse navbar-collapse" id="navbarSupportedContent">
                  <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                      <li class="nav-item">
                          <a class="nav-link active" aria-current="page" href="#">Inicio</a>
                      </li>
                      <li class="nav-item">
                          <a class="nav-link" href="#">Registra tu mascota</a>
                      </li>
                      <li class="nav-item">
                          <a class="nav-link" href="#">Adopta una mascota</a>
                      </li>
                      <li class="nav-item">
                          <a class="nav-link" href="#">Encontre una mascota</a>
                      </li>
                  </ul>
              </div>
          </div>
      </nav>
  `;
  let template = document.createElement('template');
  template.innerHTML = _nav;
  body.insertBefore(template.content, body.firstChild);
});