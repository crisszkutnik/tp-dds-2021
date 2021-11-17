-- ASOCIACIONES

INSERT INTO asociacion (id, nombre) VALUES (0, 'Patitas');
INSERT INTO asociacion (id, nombre) VALUES (1, 'Patiecitos');
INSERT INTO asociacion (id, nombre) VALUES (2, 'Los Feos');
INSERT INTO asociacion (id, nombre) VALUES (3, 'Pitufos');
-- ----------------------------------------------------------------------------------------

-- HOGARES

INSERT INTO hogares
(id, capacidad, direccion, latitud, longitud, lugares_disponibles, nombre, patio, telefono) VALUES
('0b0', 42, 'Mandu 1', -4.9, -2.7, 41, 'Hogar Escabroso', FALSE, '+123');

INSERT INTO hogares
(id, capacidad, direccion, latitud, longitud, lugares_disponibles, nombre, patio, telefono) VALUES
('0xDEAD', 69, 'Lamarca 712', 14, 27, 6, 'Hogar dulce Hogar', TRUE, '+5491166666666');

INSERT INTO hogar_caracteristicas (Hogar_id, caracteristicas)
VALUES ('0xDEAD','fuerte');

INSERT INTO hogar_caracteristicas (Hogar_id, caracteristicas)
VALUES ('0xDEAD','mandibuloso');

INSERT INTO hogar_caracteristicas (Hogar_id, caracteristicas)
VALUES ('0xDEAD','violento');

INSERT INTO hogar_caracteristicas (Hogar_id, caracteristicas)
VALUES ('0b0','crotolamo');

INSERT INTO hogar_caracteristicas (Hogar_id, caracteristicas)
VALUES ('0b0','padalustro');
-- ----------------------------------------------------------------------------------------

-- PERSONAS

INSERT INTO persona (cuil, apellido, nombre, numero_doc, tipo_doc)
VALUES ('16662', 'Baron', 'Ben', '666', 'DNI');

INSERT INTO persona (cuil, apellido, nombre, numero_doc, tipo_doc)
VALUES ('19191', 'Goncalvec', 'Juliao', '1122999', 'DNI');

INSERT INTO persona (cuil, apellido, nombre, numero_doc, tipo_doc)
VALUES ('20429333335', 'Nostradamus', 'Diente', '42933333', 'DNI');

INSERT INTO persona (cuil, apellido, nombre, numero_doc, tipo_doc)
VALUES ('20433148925', 'Sucutric', 'Cristobal', '43314892', 'DNI');
-- ----------------------------------------------------------------------------------------


-- USUARIOS

-- usuario: ben, pwd: baron
INSERT INTO usuario (username, autorizacion, password_hash, id_asociacion, cuil_persona)
VALUES ('ben', 'ADMIN', '$2a$10$yV/gDITMCLXf11vLNkSkjeMTYhwO07nzbRywE1ylcmXq.JhR9858K', NULL, '16662');

INSERT INTO usuario (username, autorizacion, password_hash, id_asociacion, cuil_persona)
VALUES ('cristobalszk', 'ADMIN', '$2a$10$weLXQ9kgzxSSxF0mcRY8lutbgcNM6wSWf9PyRGCqbERE98jELm.pa', 1, '20433148925');

-- usuario: juliao, pwd: juliao
INSERT INTO usuario (username, autorizacion, password_hash, id_asociacion, cuil_persona)
VALUES ('juliao', 'USUARIO', '$2a$10$GDUxa3ulVLUVPnxGLolArOTl0KOZs/Jg4Hv9bUp0xPBOP/rznL2ba', NULL, '19191');

-- usuario: diente, pwd: diente
INSERT INTO usuario (username, autorizacion, password_hash, id_asociacion, cuil_persona)
VALUES ('diente', 'VOLUNTARIO', '$2a$10$nb.TCco9Jhl7dTvPVPn.beW.pZuFKpwmIZ5Tdn.Zr3oL5hHTb7yVi', NULL, '20429333335');

-- ----------------------------------------------------------------------------------------

-- MASCOTAS

INSERT INTO mascota
(id, anio_nacimiento, apodo, descripcion, nombre, sexo, tamanio_mascota, tipo_animal, cuil_duenio) VALUES
(1, 2021, 'Pantu', 'Pantufla violeta castrada', 'Pantufla', 0, 2, 1, '16662');
INSERT INTO mascota_caracteristicas (Mascota_id, caracteristicas) VALUES (1, 'mandibuloso');
INSERT INTO mascota_caracteristicas (Mascota_id, caracteristicas) VALUES (1, 'pantufloso');

INSERT INTO mascota
(id, anio_nacimiento, apodo, descripcion, nombre, sexo, tamanio_mascota, tipo_animal, cuil_duenio) VALUES
(2, 2015, 'Pepito', 'Es el Papa', 'Francisco Bergoglio', 1, 2, 1, '16662');
INSERT INTO mascota_caracteristicas (Mascota_id, caracteristicas) VALUES (2, 'papal');

INSERT INTO mascota
(id, anio_nacimiento, apodo, descripcion, nombre, sexo, tamanio_mascota, tipo_animal, cuil_duenio) VALUES
(3, 2000, 'Janchu', 'La cosa mas cosa que cosa', 'Jamaico', 1, 1, 0, '19191');
INSERT INTO mascota_caracteristicas (Mascota_id, caracteristicas) VALUES (3, 'jamaiquino');
INSERT INTO mascota_caracteristicas (Mascota_id, caracteristicas) VALUES (3, 'violento');

INSERT INTO mascota
(id, anio_nacimiento, apodo, descripcion, nombre, sexo, tamanio_mascota, tipo_animal, cuil_duenio) VALUES
(4, 2003, 'Pluto', 'Perro amarillo', 'Plutarco', 0, 0, 0, '20433148925');
INSERT INTO mascota_caracteristicas (Mascota_id, caracteristicas) VALUES (4, 'mandibuloso');
INSERT INTO mascota_caracteristicas (Mascota_id, caracteristicas) VALUES (4, 'fuerte');

-- ----------------------------------------------------------------------------------------

-- MASCOTAS PERDIDAS

INSERT INTO mascotas_perdidas (id, aprobada, descripcion, latitud, longitud, id_mascota, cuil_rescatista) VALUES
(0, 0, 'Se perdio porque nacio en el año 1900.', -14.27, 27.49, NULL, '20433148925');

INSERT INTO mascotas_perdidas (id, aprobada, descripcion, latitud, longitud, id_mascota, cuil_rescatista) VALUES
(1, 1, 'Se perdió Pluto, es un perro Amarillo.', -1, 2, 4, '20429333335');

-- ----------------------------------------------------------------------------------------

-- IMAGENES

INSERT INTO imagen (nombre, id_mascota, id_mascota_perdida)
VALUES ('perdida1_0.PNG', NULL, 1);

INSERT INTO imagen (nombre, id_mascota, id_mascota_perdida)
VALUES ('perdida1_1.PNG', NULL, 1);

INSERT INTO imagen (nombre, id_mascota, id_mascota_perdida)
VALUES ('pantufla_0.jpg', 1, NULL);

INSERT INTO imagen (nombre, id_mascota, id_mascota_perdida)
VALUES ('pantufla_1.jpg', 1, NULL);

-- ----------------------------------------------------------------------------------------

-- PREGUNTAS

INSERT INTO pregunta (id, pregunta, id_asociacion)
VALUES (1, 'Pare de sufrir?', 1);

INSERT INTO pregunta (id, pregunta, id_asociacion)
VALUES (2, 'Por que quiere dejar de sufrir?', 1);

INSERT INTO pregunta (id, pregunta, id_asociacion)
VALUES (3, 'Esta es generica', NULL);

INSERT INTO pregunta_respuestas_posibles (pregunta_id, respuesta_posible)
VALUES (1, 'Sí');

INSERT INTO pregunta_respuestas_posibles (pregunta_id, respuesta_posible)
VALUES (1, 'No');

-- ----------------------------------------------------------------------------------------

-- PUBLICACIONES (DESEO ADOPTAR)

INSERT INTO publicacion_deseo_adoptar (id, fecha, vigente, cuil_persona)
VALUES (1, '2021-10-05', TRUE, '20433148925');

-- ----------------------------------------------------------------------------------------

-- PUBLICACIONES (DAR EN ADOPCION)

INSERT INTO publicacion_oferta_adopcion (id, fecha, vigente, id_asociacion, id_mascota)
VALUES (0, '2000-10-14', TRUE, 1, 1);

INSERT INTO publicacion_oferta_adopcion (id, fecha, vigente, id_asociacion, id_mascota)
VALUES (1, '2000-10-14', TRUE, 1, 3);

-- ----------------------------------------------------------------------------------------

-- RESPUESTAS

INSERT INTO respuesta (id, respuesta, id_pregunta, id_publicacion)
VALUES (0, 'Sí', 1, 1);

INSERT INTO respuesta (id, respuesta, id_pregunta, id_publicacion)
VALUES (1, 'No', 2, 1);

INSERT INTO respuesta (id, respuesta, id_pregunta, id_publicacion)
VALUES (2, 'No se tal vez', 3, 1);

INSERT INTO respuesta (id, respuesta, id_pregunta, id_publicacion)
VALUES (3, 'No', 1, 2);

INSERT INTO respuesta (id, respuesta, id_pregunta, id_publicacion)
VALUES (4, 'Porque sufrir está mal', 2, 2);

INSERT INTO respuesta (id, respuesta, id_pregunta, id_publicacion)
VALUES (5, 'Mira vos che', 3, 2);

-- ----------------------------------------------------------------------------------------

-- PREFERENCIAS

INSERT INTO preferencias (publicacion_id, sexo, tiene_patio, tipo_animal)
VALUES (1, 'MACHO', 0, 'GATO');

-- INSERT INTO preferencias (publicacion_id, sexo, tiene_patio, tipo_animal)
-- VALUES (1, 'MACHO', 0, 'GATO');

-- ----------------------------------------------------------------------------------------
