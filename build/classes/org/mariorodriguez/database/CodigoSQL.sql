set global time_zone = '-6:00';

/*Sentencias DDL*/
/*La base de datos se creara aca*/
drop database if exists DBKinalRapi;
create database DBKinalRapi;

-- Se utilizara la base de datos DBKinalRapi:
use DBKinalRapi;

-- Se crea la tabla o entidad Clientes con atributos: codigoCliente(PK), NITcliente, nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, correoCliente
create table Clientes(
	codigoCliente int not null auto_increment,
    NITcliente varchar(10) not null,
    nombreCliente varchar(50) not null,
    apellidoCliente varchar(50) not null,
    direccionCliente varchar(150) not null,
    telefonoCliente varchar(15) not null,
    correoCliente varchar(45) not null,
    primary key PK_codigoCliente(codigoCliente)
);

-- Se crea la tabla o entidad CargoEmpleado con atributos: codigoCargoEmpleado(PK), nombreCargo, descripcionCargo
create table CargoEmpleado(
	codigoCargoEmpleado int not null auto_increment,
    nombreCargo varchar(45) not null,
    descripcionCargo varchar(60) not null,
    primary key PK_codigoCargoEmpleado(codigoCargoEmpleado)
);

-- Se crea la tabla o entidad Empleados con atributos: codigoEmpleado(PK), nombreEmpleado, apellidoCliente, sueldo, direccionEmpleado, turno, CargoEmpleado_codigoCargoEmpleado(FK)
create table Empleados(
	codigoEmpleado int not null auto_increment,
    nombreEmpleado varchar(50) not null,
    apellidoEmpleado varchar(50) not null,
    sueldo decimal(10,2) not null,
    direccionEmpleado varchar(150) not null,
    turno varchar(15) not null,
    CargoEmpleado_codigoCargoEmpleado int not null,
    primary key PK_codigoEmpleado(codigoEmpleado),
    constraint FK_Empleados_CargoEmpleado foreign key Empleados(CargoEmpleado_codigoCargoEmpleado)
		references CargoEmpleado(codigoCargoEmpleado) on delete cascade
);

-- Se crea la tabla o entidad Proveedores con atributos: codigoProveedor(PK), NITproveedor, nombreProveedor, apellidoProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWeb
create table Proveedores(
	codigoProveedor int not null auto_increment,
    NITproveedor varchar(10) not null,
    nombreProveedor varchar(60) not null,
    apellidoProveedor varchar(60) not null,
    direccionProveedor varchar(150) not null,
    razonSocial varchar(60) not null,
    contactoPrincipal varchar(100) not null,
    paginaWeb varchar(50) not null,
    primary key PK_codigoProveedor(codigoProveedor)
);

-- Se crea la tabla o entidad TelefonoProveedor con atributos: codigoTelefonoProveedor(PK), numeroPrincipal, numeroSecundario, observaciones, Proveedores_codigoProveedor(FK)
create table TelefonoProveedor(
	codigoTelefonoProveedor int not null auto_increment,
    numeroPrincipal varchar(8) not null,
    numeroSecundario varchar(8),
    observaciones varchar(45),
    Proveedores_codigoProveedor int not null,
    primary key PK_codigoTelefonoProveedor(codigoTelefonoProveedor),
    constraint FK_telefonoProveedor_Proveedores foreign key telefonoProveedor(Proveedores_codigoProveedor)
		references Proveedores(codigoProveedor) on delete cascade
);

-- Se crea la tabla o entidad EmailProveedor con atributos: codigoEmailProveedor(PK), emailProveedor, descripcion, Proveedores_codigoProveedor(FK)
create table EmailProveedor(
	codigoEmailProveedor int not null auto_increment,
    emailProveedor varchar(50) not null,
    descripcion varchar(100) not null,
    Proveedores_codigoProveedor int not null,
    primary key PK_codigoEmailProveedor(codigoEmailProveedor),
    constraint FK_EmailProveedor_Proveedores foreign key EmailProveedor(Proveedores_codigoProveedor)
		references Proveedores(codigoProveedor) on delete cascade
);

-- Se crea la tabla o entidad Compras con atributos: numeroDocumento(PK), fechaDocumento, descripcion, totalDocumento
CREATE TABLE Compras (
    numeroDocumento int not null auto_increment,
    fechaDocumento date not null,
    descripcion varchar(60) not null,
    totalDocumento decimal(10,2) default 0.00,
    primary key PK_numeroDocumento (numeroDocumento)
);

-- Se crea la tabla o entidad TipoProducto con atributos: codigoTipoProducto(PK), descripcion
create table TipoProducto(
	codigoTipoProducto int not null auto_increment,
    descripcion varchar(45) not null,
    primary key PK_codigoTipoProducto(codigoTipoProducto)
);

-- Se crea la tabla o entidad Productos con atributos: codigoProducto(PK), descripcionProducto, precioUnitario, precioDocena, precioMayor, imagenProducto, existencia, TipoProducto_codigoTipoProducto(FK), Proveedores_codigoProveedor(FK)
create table Productos(
	codigoProducto varchar(15) not null,
    descripcionProducto varchar(45) not null,
    precioUnitario decimal(10,2) default 0.00,
    precioDocena decimal(10,2) default 0.00,
    precioMayor decimal(10,2) default 0.00,
    imagenProducto varchar(45),
    existencia int,
    TipoProducto_codigoTipoProducto int not null,
    Proveedores_codigoProveedor int not null,
    primary key PK_codigoProducto(codigoProducto),
    constraint FK_Productos_TipoProducto foreign key Productos(TipoProducto_codigoTipoProducto)
		references TipoProducto(codigoTipoProducto) on delete cascade,
    constraint FK_Productos_Proveedores foreign key Productos(Proveedores_codigoProveedor)
		references Proveedores(codigoProveedor) on delete cascade
);

-- Se crea la tabla o entidad DetalleCompra con atributos: codigoDetalleCompra(PK), costoUnitario, cantidad, Productos_codigoProducto(FK), Compras_numeroDocumento(FK)
create table DetalleCompra(
	codigoDetalleCompra int not null auto_increment,
    costoUnitario decimal(10,2) not null,
    cantidad int not null,
    Productos_codigoProducto varchar(15) not null,
    Compras_numeroDocumento int not null,
    primary key PK_codigoDetalleCompra(codigoDetalleCompra),
    constraint FK_DetalleCompra_Compras foreign key DetalleCompra(Compras_numeroDocumento)
		references Compras(numeroDocumento) on delete cascade,
	constraint FK_DetalleCompra_Productos foreign key DetalleCompra(Productos_codigoProducto)
		references Productos(codigoProducto) on delete cascade
);

-- Se crea la tabla o entidad Factura con atributos: numeroFactura(PK), estado, totalFactura, fechaFactura, Clientes_codigoCliente(FK), Empleados_codigoEmpleado(FK)
create table Factura(
	numeroFactura int not null auto_increment,
    estado varchar(50) not null,
    totalFactura decimal(10,2) default 0.00,
    fechaFactura varchar(45),
    Clientes_codigoCliente int not null,
    Empleados_codigoEmpleado int not null,
    primary key PK_numeroFactura(numeroFactura),
    constraint FK_Factura_Clientes foreign key Factura(Clientes_codigoCliente)
		references Clientes(codigoCliente) on delete cascade,
    constraint FK_Factura_Empleados foreign key Factura(Empleados_codigoEmpleado)
		references Empleados(codigoEmpleado) on delete cascade
);

-- Se crea la tabla o entidad DetalleFactura con atributos: codigoDetalleFactura(PK), precioUnitario, cantidad, Factura_numeroFactura(FK), Productos_codigoProducto(FK)
create table DetalleFactura(
	codigoDetalleFactura int not null auto_increment,
    precioUnitario decimal(10,2),
    cantidad int not null,
    Factura_numeroFactura int not null,
    Productos_codigoProducto varchar(15) not null,
    primary key PK_codigoDetalleFactura(codigoDetalleFactura),
    constraint FK_DetalleFactura_Compras foreign key DetalleFactura(Factura_numeroFactura)
		references Factura(numeroFactura) on delete cascade,
	constraint FK_DetalleFactura_Productos foreign key DetalleFactura(Productos_codigoProducto)
		references Productos(codigoProducto) on delete cascade
);


-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Procedimientos almacenados -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Clientes -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- AGREGAR -- 

Delimiter $$

	create procedure sp_AgregarClientes(
    in NITcliente varchar(10),
    in nombreCliente varchar(50),
    in apellidoCliente varchar(50),
    in direccionCliente varchar(150),
    in telefonoCliente varchar(15),
    in correoCliente varchar(45))
		Begin
			Insert into Clientes(
			NITcliente,
			nombreCliente,
			apellidoCliente,
			direccionCliente,
			telefonoCliente,
			correoCliente) values (
			NITcliente,
			nombreCliente,
			apellidoCliente,
			direccionCliente,
			telefonoCliente,
			correoCliente);
		End $$
    
Delimiter ;                  

-- LISTAR --

Delimiter $$

	create procedure sp_ListarClientes()
		Begin
			select
            C.codigoCliente,
			C.NITcliente,
			C.nombreCliente,
			C.apellidoCliente,
			C.direccionCliente,
			C.telefonoCliente,
			C.correoCliente
            from Clientes C;
        End $$
        
Delimiter ;

-- BUSCAR -- 

Delimiter $$

	create procedure sp_BuscarClientes(in codCli int)
		Begin
			select
            C.codigoCliente,
			C.NITcliente,
			C.nombreCliente,
			C.apellidoCliente,
			C.direccionCliente,
			C.telefonoCliente,
			C.correoCliente
            from Clientes C
            where codigoCliente = codCli;
        End $$
        
Delimiter ;

-- ELIMINAR --

Delimiter $$

	create procedure sp_EliminarClientes(in codCli int)
		Begin
			Delete from Clientes
				where codigoCliente = codCli;
        End $$
        
Delimiter ;

-- EDITAR --

Delimiter $$

	create procedure sp_EditarClientes(in codCli int, in nCliente varchar(10), in nomClientes varchar(50), in apCliente varchar(50),
			in direcCliente varchar(150), in telCliente varchar(15), in corrCliente varchar(45))
		Begin
			Update Clientes C
				set
				C.NITcliente = nCliente,
				C.nombreCliente = nomClientes,
				C.apellidoCliente = apCliente,
				C.direccionCliente = direcCliente,
				C.telefonoCliente = telCliente,
				C.correoCliente = corrCliente
                where codigoCliente = codCli;
		End $$
        
Delimiter ;

-- Reinicar contador ID Clientes --

Delimiter $$

	create procedure sp_reinicioIdClientes()
		Begin
			alter table Clientes auto_increment = 1;
		End $$
        
Delimiter ;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- Llamado de procedimientos almacenados -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -

-- Agregar clientes --

call sp_AgregarClientes('114006350', 'Juan', 'Rivas', 'Zona 7', '23002626', 'mrodriguez-2023292@kinal.edu.gt');
call sp_AgregarClientes('254849542', 'Pablo', 'Guillen', 'Zona 18', '54861278', 'dmorente-2023242@kinal.edu.gt');

-- Listar clientes --

call sp_ListarClientes();

-- Buscar cliente --

call sp_BuscarClientes(1);

-- Eliminar cliente --

call sp_EliminarClientes(1);

   -- Comprobacion --

	  call sp_ListarClientes();
      
-- Editar cliente --

call sp_EditarClientes(2, '894654641', 'Ceferino', 'Rodriguez', 'Zona 32', '55813654', 'cef23152@kinal.edu.gt');

	-- Comprobacion --
    
	   call sp_ListarClientes();
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

       
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Procedimientos almacenados -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Proveedores -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- AGREGAR -- 

Delimiter $$

	create procedure sp_AgregarProveedores(
    in NITproveedor varchar(10),
    in nombreProveedor varchar(60),
    in apellidoProveedor varchar(60),
    in direccionProveedor varchar(150),
    in razonSocial varchar(60),
    in contactoPrincipal varchar(100),
    in paginaWeb varchar(50))
		Begin
			Insert into Proveedores(
			NITproveedor,
			nombreProveedor,
			apellidoProveedor,
			direccionProveedor,
			razonSocial,
            contactoPrincipal,
			paginaWeb) values (
			NITproveedor,
			nombreProveedor,
			apellidoProveedor,
			direccionProveedor,
			razonSocial,
            contactoPrincipal,
			paginaWeb);
            
		End $$
    
Delimiter ;                  

-- LISTAR --

Delimiter $$

	create procedure sp_ListarProveedores()
		Begin
			select
            P.codigoProveedor,
            P.NITproveedor,
			P.nombreProveedor,
			P.apellidoProveedor,
			P.direccionProveedor,
			P.razonSocial,
            P.contactoPrincipal,
			P.paginaWeb
            from Proveedores P;
        End $$
        
Delimiter ;

-- BUSCAR --

Delimiter $$

	create procedure sp_BuscarProveedores(in codPro int)
		Begin
			select
            P.codigoProveedor,
            P.NITproveedor,
			P.nombreProveedor,
			P.apellidoProveedor,
			P.direccionProveedor,
			P.razonSocial,
            P.contactoPrincipal,
			P.paginaWeb
            from Proveedores P
            where codigoProveedor = codPro;
        End $$
        
Delimiter ;

-- ELIMINAR --

Delimiter $$

	create procedure sp_EliminarProveedores(in codPro int)
		Begin
			Delete from Proveedores
				where codigoProveedor = codPro;
        End $$
        
Delimiter ;

-- EDITAR --

Delimiter $$

	create procedure sp_EditarProveedores(in codPro int, in nPro varchar(10), in nomPro varchar(60), in apPro varchar(60),
			in direcPro varchar(150), in raSocial varchar(60), in conPrin varchar(100), in pagWeb varchar(50))
		Begin
			Update Proveedores P
				set
				P.NITproveedor = nPro,
				P.nombreProveedor = nomPro,
				P.apellidoProveedor = apPro,
				P.direccionProveedor = direcPro,
				P.razonSocial = raSocial,
				P.contactoPrincipal = conPrin,
                P.paginaWeb = pagWeb
                where codigoProveedor = codPro;
		End $$
        
Delimiter ;

-- Reinicar contador ID Proveedor --

Delimiter $$

	create procedure sp_reinicioIdProveedores()
		Begin
			alter table Proveedores auto_increment = 1;
		End $$
        
Delimiter ;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- Llamado de procedimientos almacenados -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -

-- Agregar proveedores --

call sp_AgregarProveedores('114006350', 'Alejandro ', 'Martínez', 'Zona 7', 'TechSolutions S.A. de C.V.', 'María García','www.innovatetechsolutions.com');
call sp_AgregarProveedores('949474654', 'Sofia ', 'Rodriguez', 'Zona 8', 'GlobalTech Solutions, Inc.', 'Marco Ortiz','www.succionaproblemas.com');

-- Listar proveedores --

call sp_ListarProveedores();

-- Buscar proveedores --

call sp_BuscarProveedores(1);

-- Eliminar proveedor --

call sp_EliminarProveedores(1);

   -- Comprobacion --
   
	  call sp_ListarProveedores();

-- Editar proveedor --

call sp_EditarProveedores(2, '000000000', 'Diego', 'Herrera', 'Zona 0', 'Manimalista S.A.', 'Juan Rivas','www.manimalista.com');

	-- Comprobacion --
    
	   call sp_ListarProveedores();
       
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --


-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Procedimientos almacenados -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Compras -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- - -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- AGREGAR -- 

Delimiter $$

	create procedure sp_AgregarCompras(
    in fechaDocumento date,
    in descripcion varchar(60))
		Begin
			Insert into Compras(
			fechaDocumento,
			descripcion) values (
			fechaDocumento,
			descripcion);
		End $$
    
Delimiter ;                  

-- LISTAR --

Delimiter $$

	create procedure sp_ListarCompras()
		Begin
			select
            C.numeroDocumento,
            C.fechaDocumento,
			C.descripcion,
			C.totalDocumento
            from Compras C;
        End $$
        
Delimiter ;

-- BUSCAR -- 

Delimiter $$
	create procedure sp_BuscarCompras(in numDoc int)
		Begin
			select
            C.numeroDocumento,
            C.fechaDocumento,
			C.descripcion,
			C.totalDocumento
            from Compras C
            where numeroDocumento = numDoc;
        End $$
Delimiter ;

-- ELIMINAR --

Delimiter $$
	create procedure sp_EliminarCompras(in numDoc int)
		Begin
			Delete from Compras
				where numeroDocumento = numDoc;
        End $$
Delimiter ;

-- EDITAR --

Delimiter $$
	create procedure sp_EditarCompras(in numDoc int, in feDoc date, in descripcion varchar(60))
		Begin
			Update Compras C
				set
				 C.fechaDocumento = feDoc,
				 C.descripcion = descripcion
                where numeroDocumento = numDoc;
		End $$
Delimiter ;

-- Reinicar contador ID Compras --

Delimiter $$
	create procedure sp_reinicioIdCompras()
		Begin
			alter table Compras auto_increment = 1;
		End $$
Delimiter ;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- Llamado de procedimientos almacenados -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -

-- Agregar compras --

call sp_AgregarCompras('2002-10-01', 'Es el mejor peluche que vas a ver en tu vida');
call sp_AgregarCompras('2007-05-30', 'Compraste la espada de un maestro perdido');

-- Listar compras --

call sp_ListarCompras();

-- Buscar compras --

call sp_BuscarCompras(1);

-- Eliminar compras --

call sp_EliminarCompras(1);

   -- Comprobacion --

	  call sp_ListarCompras();
      
-- Editar compras --

call sp_EditarCompras(2, '2002-02-12', 'Es el terreneitor');

	-- Comprobacion --
    
	   call sp_ListarCompras();
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --


-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Procedimientos almacenados -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Tipo de producto -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- ---
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- AGREGAR -- 

Delimiter $$

	create procedure sp_AgregarTipoProductos(
    in descripcion varchar(45))
		Begin
			Insert into TipoProducto(
			descripcion) values (
			descripcion);
		End $$
    
Delimiter ;                  

-- LISTAR --

Delimiter $$

	create procedure sp_ListarTipoProductos()
		Begin
			select
            TP.codigoTipoProducto,
            TP.descripcion
            from TipoProducto TP;
        End $$
        
Delimiter ;

-- BUSCAR -- 

Delimiter $$
	create procedure sp_BuscarTipoProductos(in codTP int)
		Begin
			select
            TP.codigoTipoProducto,
            TP.descripcion
            from TipoProducto TP
            where codigoTipoProducto = codTP;
        End $$
Delimiter ;

-- ELIMINAR --

Delimiter $$
	create procedure sp_EliminarTipoProductos(in codTP int)
		Begin
			Delete from TipoProducto
				where codigoTipoProducto = codTP;
        End $$
Delimiter ;

-- EDITAR --

Delimiter $$
	create procedure sp_EditarTipoProductos(in codTP int, in des varchar(45))
		Begin
			Update TipoProducto TP
				set
				 TP.descripcion = des
                where codigoTipoProducto = codTP;
		End $$
Delimiter ;

-- Reinicar contador ID TipoProductos --

Delimiter $$
	create procedure sp_reinicioIdTipoProductos()
		Begin
			alter table TipoProducto auto_increment = 1;
		End $$
Delimiter ;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- Llamado de procedimientos almacenados -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -

-- Agregar tipo de productos --

call sp_AgregarTipoProductos('Es de un tipo indestructible');
call sp_AgregarTipoProductos('Se considera peligroso');

-- Listar tipo de producto --

call sp_ListarTipoProductos();

-- Buscar tipo de producto --

call sp_BuscarTipoProductos(1);

-- Eliminar tipo de producto --

call sp_EliminarTipoProductos(1);

   -- Comprobacion --

	  call sp_ListarTipoProductos();
      
-- Editar tipo de producto --

call sp_EditarTipoProductos(2, 'Se considera de rango S');

	-- Comprobacion --
    
	   call sp_ListarTipoProductos();
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --


-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Procedimientos almacenados -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Cargo empleado -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- AGREGAR -- 

Delimiter $$

	create procedure sp_AgregarCargoEmpleado(
    in nombreCargo varchar(45),
    in descripcionCargo varchar(60))
		Begin
			Insert into CargoEmpleado(
			nombreCargo,
            descripcionCargo) values (
			nombreCargo,
            descripcionCargo);
		End $$
    
Delimiter ;                  

-- LISTAR --

Delimiter $$

	create procedure sp_ListarCargoEmpleado()
		Begin
			select
            CE.codigoCargoEmpleado,
            CE.nombreCargo,
            CE.descripcionCargo
            from CargoEmpleado CE;
        End $$
        
Delimiter ;

-- BUSCAR -- 

Delimiter $$
	create procedure sp_BuscarCargoEmpleado(in codCE int)
		Begin
			select
            CE.codigoCargoEmpleado,
            CE.nombreCargo,
            CE.descripcionCargo
            from CargoEmpleado CE
            where codigoCargoEmpleado = codCE;
        End $$
Delimiter ;

-- ELIMINAR --

Delimiter $$
	create procedure sp_EliminarCargoEmpleado(in codCE int)
		Begin
			Delete from CargoEmpleado
				where codigoCargoEmpleado = codCE;
        End $$
Delimiter ;

-- EDITAR --

Delimiter $$
	create procedure sp_EditarCargoEmpleado(in codCE int, in nomCargo varchar(45), in desCargo varchar(60))
		Begin
			Update CargoEmpleado CE
				set
				 CE.nombreCargo = nomCargo,
                 CE.descripcionCargo = desCargo
                where codigoCargoEmpleado = codCE;
		End $$
Delimiter ;

-- Reinicar contador ID CargoEmpleado --

Delimiter $$
	create procedure sp_reinicioIdCargoEmpleado()
		Begin
			alter table CargoEmpleado auto_increment = 1;
		End $$
Delimiter ;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- Llamado de procedimientos almacenados -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -

-- Agregar cargo empleado --

call sp_AgregarCargoEmpleado('Ingeniero de Software', 'Diseña, desarrolla y software.');
call sp_AgregarCargoEmpleado('Analista de Marketing Digital', 'Analiza datos y tendencias del mercado.');

-- Listar cargo empleado --

call sp_ListarCargoEmpleado();

-- Buscar cargo empleado --

call sp_BuscarCargoEmpleado(1);

-- Eliminar cargo empleado --

call sp_EliminarCargoEmpleado(1);

   -- Comprobacion --

	  call sp_ListarCargoEmpleado();
      
-- Editar cargo empleado --

call sp_EditarCargoEmpleado(2, 'Diseñador grafico', 'Diseña mensajes efectivos y atractivos para la gente.');

	-- Comprobacion --
    
	   call sp_ListarCargoEmpleado();
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --


-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Procedimientos almacenados -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Productos -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- AGREGAR -- 

Delimiter $$

	create procedure sp_AgregarProductos(
    in codigoProducto varchar(15),
    in descripcionProducto varchar(45),
    in precioUnitario decimal(10,2),
    in precioDocena decimal(10,2),
    in precioMayor decimal(10,2),
    in existencia int,
    in TipoProducto_codigoTipoProducto int,
    in Proveedores_codigoProveedor int
    )
		Begin
			Insert into Productos(
            codigoProducto,
			descripcionProducto,
            precioUnitario,
			precioDocena,
			precioMayor,
			existencia,
			TipoProducto_codigoTipoProducto,
			Proveedores_codigoProveedor) values (
            codigoProducto,
			descripcionProducto,
            precioUnitario,
			precioDocena,
			precioMayor,
			existencia,
			TipoProducto_codigoTipoProducto,
			Proveedores_codigoProveedor);
		End $$
    
Delimiter ;                  

-- LISTAR --

Delimiter $$

	create procedure sp_ListarProductos()
		Begin
			select
            Pro.codigoProducto,
            Pro.descripcionProducto,
			Pro.precioUnitario,
			Pro.precioDocena,
			Pro.precioMayor,
			Pro.imagenProducto,
			Pro.existencia,
			Pro.TipoProducto_codigoTipoProducto,
			Pro.Proveedores_codigoProveedor
            from Productos Pro;
        End $$
        
Delimiter ;

-- BUSCAR -- 

Delimiter $$
	create procedure sp_BuscarProducto(in codPro varchar(15))
		Begin
			select
            Pro.codigoProducto,
            Pro.descripcionProducto,
			Pro.precioUnitario,
			Pro.precioDocena,
			Pro.precioMayor,
			Pro.imagenProducto,
			Pro.existencia,
			Pro.TipoProducto_codigoTipoProducto,
			Pro.Proveedores_codigoProveedor
            from Productos Pro
            where codigoProducto = codPro;
        End $$
Delimiter ;

-- ELIMINAR --

Delimiter $$
	create procedure sp_EliminarProducto(in codPro varchar(15))
		Begin
			Delete from Productos
				where codigoProducto = codPro;
        End $$
Delimiter ;

-- EDITAR --

Delimiter $$
	create procedure sp_EditarProducto(in codPro varchar(15), in desPro varchar(45), in preU decimal(10,2), in preD decimal(10,2), in preM decimal(10,2), in exis int, in TPro_codTP int, in Pro_codPro int)
		Begin
			Update Productos Pro
				set
				 Pro.descripcionProducto = desPro,
                 Pro.precioUnitario = preU,
                 Pro.precioDocena = preD,
                 Pro.precioMayor = preM,
				 Pro.existencia = exis,
				 Pro.TipoProducto_codigoTipoProducto = TPro_codTP,
				 Pro.Proveedores_codigoProveedor = Pro_codPro
                where codigoProducto = codPro;
		End $$
Delimiter ;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- Llamado de procedimientos almacenados -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -

-- Agregar producto --

	-- Necesario para insertar correctamente los datos --
	call sp_AgregarTipoProductos('Es de un tipo indestructible');
	call sp_AgregarProveedores('114006350', 'Alejandro ', 'Martínez', 'Zona 7', 'TechSolutions S.A. de C.V.', 'María García','www.innovatetechsolutions.com');

call sp_AgregarProductos('ASD-48', 'Un brebaje mágico que promete rejuvenecer.', 10.00, 20.00, 50.00, 1275, 2, 2);
call sp_AgregarProductos('DFE-15', 'Dispositivo que emite destellos de luz.', 5.00, 25.00, 100.00, 2500, 3, 3);

-- Listar productos --

call sp_ListarProductos();

-- Buscar producto --

call sp_BuscarProducto('ASD-48');

-- Eliminar producto --

call sp_EliminarProducto('ASD-48');

   -- Comprobacion --

	  call sp_ListarProductos();
      
-- Editar producto --

call sp_EditarProducto('DFE-15', 'Dispositivo que emite destellos de luz.', 7.00, 14.00, 144.00, 5000, 3, 2);

	-- Comprobacion --
    
	   call sp_ListarProductos();
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Trigger -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- - Detalle compra -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -

DELIMITER $$

	create trigger totalCompra_DetalleCompra
	before update on DetalleCompra
	for each row
		begin
			declare total_comprado decimal(10, 2);

			-- Calcula el total gastado y la cantidad comprada del producto
			set total_comprado = new.costoUnitario * new.cantidad;
			
			-- Actualiza los precios en Productos
			update Compras
			set Compras.totalDocumento = total_comprado
			where Compras.numeroDocumento = new.codigoDetalleCompra;
		END $$

DELIMITER ;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Procedimientos almacenados -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Detalle compra -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- AGREGAR -- 

Delimiter $$

	create procedure sp_AgregarDetalleCompra(
    in costoUnitario decimal(10,2),
    in cantidad int,
    in Productos_codigoProducto varchar(15),
    in Compras_numeroDocumento int
    )
		Begin
			Insert into DetalleCompra(
            costoUnitario,
			cantidad,
			Productos_codigoProducto,
			Compras_numeroDocumento) values (
            costoUnitario,
			cantidad,
			Productos_codigoProducto,
			Compras_numeroDocumento);
		End $$
    
Delimiter ;                  

-- LISTAR --

Delimiter $$

	create procedure sp_ListarDetalleCompra()
		Begin
			select
            DC.codigoDetalleCompra,
            DC.costoUnitario,
			DC.cantidad,
			DC.Productos_codigoProducto,
			DC.Compras_numeroDocumento
            from DetalleCompra DC;
        End $$
        
Delimiter ;

-- BUSCAR -- 

Delimiter $$
	create procedure sp_BuscarDetalleCompra(in codDC int)
		Begin
			select
            DC.codigoDetalleCompra,
            DC.costoUnitario,
			DC.cantidad,
			DC.Productos_codigoProducto,
			DC.Compras_numeroDocumento
            from DetalleCompra DC
            where codigoDetalleCompra = codDC;
        End $$
Delimiter ;

-- ELIMINAR --

Delimiter $$
	create procedure sp_EliminarDetalleCompra(in codDC int)
		Begin
			Delete from DetalleCompra
				where codigoDetalleCompra = codDC;
        End $$
Delimiter ;

-- EDITAR --

Delimiter $$
	create procedure sp_EditarDetalleCompra(in codDC int, in cosUni decimal(10,2), in can int, in Pro_codP varchar(15), in Com_numDoc int)
		Begin
			Update DetalleCompra DC
				set
				 DC.costoUnitario = cosUni,
				 DC.cantidad = can,
				 DC.Productos_codigoProducto = Pro_codP,
				 DC.Compras_numeroDocumento = Com_numDoc
                where codigoDetalleCompra = codDC;
		End $$
Delimiter ;

-- Reinicar contador ID Detalle compra --

Delimiter $$
	create procedure sp_reinicioIdDetalleCompra()
		Begin
			alter table DetalleCompra auto_increment = 1;
		End $$
Delimiter ;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- Llamado de procedimientos almacenados -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -

-- Agregar detalle compra --

call sp_AgregarDetalleCompra(100.00, 2, 'DFE-15', 2);
call sp_AgregarDetalleCompra(20.00, 3, 'DFE-15', 2);

-- Listar detalle compra --

call sp_ListarDetalleCompra();

-- Buscar detalle compra --

call sp_BuscarDetalleCompra(1);

-- Eliminar detalle compra --

call sp_EliminarDetalleCompra(1);

   -- Comprobacion --

	  call sp_ListarDetalleCompra();
      
-- Editar detalle compra --

call sp_EditarDetalleCompra(2, 10.00, 1, 'DFE-15', 2);

	-- Comprobacion --
    
	   call sp_ListarDetalleCompra();
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --


-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- ---
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- - Trigger -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- - -- Factura -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- ---

call sp_EditarDetalleCompra(2, 10.00, 1, 'DFE-15', 2);

DELIMITER $$

	create trigger totalFactura_Factura
	after update on DetalleCompra
	for each row
		begin
			declare total_comprado decimal(10, 2);

			-- Calcula el total gastado y la cantidad comprada del producto
			set total_comprado = new.costoUnitario * new.cantidad;
			
			-- Actualiza los precios en Productos
			update Factura
			set Factura.totalFactura = total_comprado
			where Factura.numeroFactura = new.codigoDetalleCompra;
		END $$

DELIMITER ;


-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Procedimientos almacenados -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Empleados -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- AGREGAR -- 

Delimiter $$

	create procedure sp_AgregarEmpleado(
    in nombreEmpleado varchar(50),
    in apellidoEmpleado varchar(50),
    in sueldo decimal(10,2),
    in direccionEmpleado varchar(150),
    in turno varchar(15),
    in CargoEmpleado_codigoCargoEmpleado int)
		Begin
			Insert into Empleados(
			nombreEmpleado,
		    apellidoEmpleado,
			sueldo,
			direccionEmpleado,
			turno,
			CargoEmpleado_codigoCargoEmpleado) values (
			nombreEmpleado,
		    apellidoEmpleado,
			sueldo,
			direccionEmpleado,
			turno,
			CargoEmpleado_codigoCargoEmpleado);
		End $$
    
Delimiter ;                  

-- LISTAR --

Delimiter $$

	create procedure sp_ListarEmpleados()
		Begin
			select
            E.codigoEmpleado,
            E.nombreEmpleado,
		    E.apellidoEmpleado,
			E.sueldo,
			E.direccionEmpleado,
			E.turno,
			E.CargoEmpleado_codigoCargoEmpleado
            from Empleados E;
        End $$
        
Delimiter ;

-- BUSCAR -- 

Delimiter $$

	create procedure sp_BuscarEmpleado(in codEm int)
		Begin
			select
            E.codigoEmpleado,
            E.nombreEmpleado,
		    E.apellidoEmpleado,
			E.sueldo,
			E.direccionEmpleado,
			E.turno,
			E.CargoEmpleado_codigoCargoEmpleado
            from Empleados E
            where codigoEmpleado = codEm;
        End $$
        
Delimiter ;

-- ELIMINAR --

Delimiter $$

	create procedure sp_EliminarEmpleado(in codEm int)
		Begin
			Delete from Empleados
				where codigoEmpleado = codEm;
        End $$
        
Delimiter ;

-- EDITAR --

Delimiter $$

	create procedure sp_EditarEmpleado(in codEm int, in nomEm varchar(50), in apEm varchar(50), in sueldo decimal(10,2),
			in dircEm varchar(150), in turno varchar(15), in CE_codCE int)
		Begin
			Update Empleados E
				set
				E.nombreEmpleado = nomEm,
				E.apellidoEmpleado = apEm,
				E.sueldo = sueldo,
				E.direccionEmpleado = dircEm,
				E.turno = turno,
				E.CargoEmpleado_codigoCargoEmpleado = CE_codCE
                where codigoEmpleado = codEm;
		End $$
        
Delimiter ;

-- Reinicar contador ID Empleados --

Delimiter $$

	create procedure sp_reinicioIdEmpleados()
		Begin
			alter table Empleados auto_increment = 1;
		End $$
        
Delimiter ;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- Llamado de procedimientos almacenados -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -

-- Agregar empleado --
	-- necesario para que funcione correctamente --
    call sp_AgregarCargoEmpleado('Ingeniero de Software', 'Diseña, desarrolla y software.');

call sp_AgregarEmpleado('Albert', 'Einstein', 3500.00, 'Zona 36', 'Matutina', 2);
call sp_AgregarEmpleado('Oprah', 'Winfrey', 2450.00, 'Zona 12', 'Vespertina', 3);

-- Listar empleados --

call sp_ListarEmpleados();

-- Buscar empleado --

call sp_BuscarEmpleado(1);

-- Eliminar empleado --

call sp_EliminarEmpleado(1);

   -- Comprobacion --

	  call sp_ListarEmpleados();
      
-- Editar empleado --

call sp_EditarEmpleado(2, 'Rafael', 'Rivas', 7000.00, 'Zona 10', 'Matutina', 2);

	-- Comprobacion --
    
	   call sp_ListarEmpleados();
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --


-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Procedimientos almacenados -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Factura -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- ---
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- AGREGAR -- 

Delimiter $$

	create procedure sp_AgregarFactura(
    in estado varchar(50),
    in fechaFactura varchar(45),
    in Clientes_codigoCliente int,
    in Empleados_codigoEmpleado int)
		Begin
			Insert into Factura(
			estado,
			fechaFactura,
			Clientes_codigoCliente,
			Empleados_codigoEmpleado) values (
			estado,
			fechaFactura,
			Clientes_codigoCliente,
			Empleados_codigoEmpleado);
		End $$
    
Delimiter ;                  

-- LISTAR --

Delimiter $$

	create procedure sp_ListarFacturas()
		Begin
			select
            F.numeroFactura,
            F.estado,
			F.totalFactura,
			F.fechaFactura,
			F.Clientes_codigoCliente,
			F.Empleados_codigoEmpleado
            from Factura F;
        End $$
        
Delimiter ;

-- BUSCAR -- 

Delimiter $$

	create procedure sp_BuscarFactura(in codF int)
		Begin
			select
            F.numeroFactura,
            F.estado,
			F.totalFactura,
			F.fechaFactura,
			F.Clientes_codigoCliente,
			F.Empleados_codigoEmpleado
            from Factura F
            where numeroFactura = codF;
        End $$
        
Delimiter ;

-- ELIMINAR --

Delimiter $$

	create procedure sp_EliminarFactura(in codF int)
		Begin
			Delete from Factura
				where numeroFactura = codF;
        End $$
        
Delimiter ;

-- EDITAR --

Delimiter $$

	create procedure sp_EditarFactura(in codF int, in es varchar(50), in feF varchar(45),
			in C_codC int, in E_codE int)
		Begin
			Update Factura F
				set
				F.estado = es,
				F.fechaFactura = feF,
				F.Clientes_codigoCliente = C_codC,
				F.Empleados_codigoEmpleado = E_codE
                where numeroFactura = codF;
		End $$
        
Delimiter ;

-- Reinicar contador ID Factura --

Delimiter $$

	create procedure sp_reinicioIdFactura()
		Begin
			alter table Factura auto_increment = 1;
		End $$
        
Delimiter ; 

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- Llamado de procedimientos almacenados -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -

-- Agregar factura --
	-- necesario para que funcione correctamente --
    call sp_AgregarClientes('114006350', 'Juan', 'Rivas', 'Zona 7', '23002626', 'mrodriguez-2023292@kinal.edu.gt');
	call sp_AgregarEmpleado('Oprah', 'Winfrey', 2450.00, 'Zona 12', 'Vespertina', 3);

call sp_AgregarFactura('Malo', '2024-07-14', 2, 2);
call sp_AgregarFactura('Bueno', '2024-04-12', 3, 3);

-- Listar factura --

call sp_ListarFacturas();

-- Buscar factura --

call sp_BuscarFactura(1);

-- Eliminar factura --

call sp_EliminarFactura(1);

   -- Comprobacion --

	  call sp_ListarFacturas();
      
-- Editar factura --

call sp_EditarFactura(2, 'Buenisimo', '2024-12-24', 2, 2);

	-- Comprobacion --
    
	   call sp_ListarFacturas();
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --


-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Procedimientos almacenados -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- - Detalle factura -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- AGREGAR -- 

Delimiter $$

	create procedure sp_AgregarDetalleFactura(
    in precioUnitario decimal(10,2),
    in cantidad int,
    in Factura_numeroFactura int,
    in Productos_codigoProducto varchar(15))
		Begin
			Insert into DetalleFactura(
			precioUnitario,
			cantidad ,
			Factura_numeroFactura,
			Productos_codigoProducto) values (
			precioUnitario,
			cantidad ,
			Factura_numeroFactura,
			Productos_codigoProducto);
		End $$
    
Delimiter ;                  

-- LISTAR --

Delimiter $$

	create procedure sp_ListarDetalleFacturas()
		Begin
			select
            DF.codigoDetalleFactura,
            DF.precioUnitario,
			DF.cantidad,
			DF.Factura_numeroFactura,
			DF.Productos_codigoProducto
            from DetalleFactura DF;
        End $$
        
Delimiter ;

-- BUSCAR -- 

Delimiter $$

	create procedure sp_BuscarDetalleFactura(in codDF int)
		Begin
			select
			DF.codigoDetalleFactura,
            DF.precioUnitario,
			DF.cantidad,
			DF.Factura_numeroFactura,
			DF.Productos_codigoProducto
            from DetalleFactura DF
            where codigoDetalleFactura = codDF;
        End $$
        
Delimiter ;

-- ELIMINAR --

Delimiter $$

	create procedure sp_EliminarDetalleFactura(in codDF int)
		Begin
			Delete from DetalleFactura
				where codigoDetalleFactura = codDF;
        End $$
        
Delimiter ;

-- EDITAR --

Delimiter $$

	create procedure sp_EditarDetalleFactura(in codDF int, in precioU decimal(10,2), in cantidad int,
			in F_numF int, in P_codP varchar(15))
		Begin
			Update DetalleFactura DF
				set
				DF.precioUnitario = precioU,
				DF.cantidad = cantidad,
				DF.Factura_numeroFactura = F_numF,
				DF.Productos_codigoProducto = P_codP
                where codigoDetalleFactura = codDF;
		End $$
        
Delimiter ;

-- Reinicar contador ID Detalle factura --

Delimiter $$

	create procedure sp_reinicioIdDetalleFactura()
		Begin
			alter table DetalleFactura auto_increment = 1;
		End $$
        
Delimiter ; 

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- Llamado de procedimientos almacenados -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -

-- Agregar detalle factura --
	-- necesario para que funcione correctamente --
	call sp_AgregarFactura('Bueno', '2024-04-12', 3, 3);
	call sp_AgregarProductos('ASD-48', 'Un brebaje mágico que promete rejuvenecer.', 6.00, 12.00, 90.00, 1275, 2, 2);

call sp_AgregarDetalleFactura(10.00, 4, 2, 'DFE-15');
call sp_AgregarDetalleFactura(10.00, 5, 3, 'ASD-48');

-- Listar detalle actura --

call sp_ListarDetalleFacturas();

-- Buscar detalle factura --

call sp_BuscarDetalleFactura(1);

-- Eliminar detalle factura --

call sp_EliminarDetalleFactura(1);

   -- Comprobacion --

	  call sp_ListarDetalleFacturas();
      
-- Editar detalle factura --

call sp_EditarDetalleFactura(2, 11.00, 17, 3, 'ASD-48');

	-- Comprobacion --
    
	   call sp_ListarDetalleFacturas();
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --


-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Procedimientos almacenados -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Email proveedor -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- AGREGAR -- 

Delimiter $$

	create procedure sp_AgregarEmailProveedor(
    in emailProveedor varchar(50),
    in descripcion varchar(100),
    in Proveedores_codigoProveedor int)
		Begin
			Insert into EmailProveedor(
			emailProveedor,
			descripcion,
			Proveedores_codigoProveedor) values (
			emailProveedor,
			descripcion,
			Proveedores_codigoProveedor);
		End $$
    
Delimiter ;                  

-- LISTAR --

Delimiter $$

	create procedure sp_ListarEmailProveedores()
		Begin
			select
			EP.codigoEmailProveedor,
			EP.emailProveedor,
			EP.descripcion,
			EP.Proveedores_codigoProveedor
            from EmailProveedor EP;
        End $$
        
Delimiter ;

-- BUSCAR -- 

Delimiter $$

	create procedure sp_BuscarEmailProveedor(in codEP int)
		Begin
			select
			EP.codigoEmailProveedor,
			EP.emailProveedor,
			EP.descripcion,
			EP.Proveedores_codigoProveedor
            from EmailProveedor EP
            where codigoEmailProveedor = codEP;
        End $$
        
Delimiter ;

-- ELIMINAR --

Delimiter $$

	create procedure sp_EliminarEmailProveedor(in codEP int)
		Begin
			Delete from EmailProveedor
				where codigoEmailProveedor = codEP;
        End $$
        
Delimiter ;

-- EDITAR --

Delimiter $$

	create procedure sp_EditarEmailProveedor(in codEP int, in emailP varchar(50), in des varchar(100),
			in P_codP int)
		Begin
			Update EmailProveedor EP
				set
				EP.emailProveedor = emailP,
				EP.descripcion = des,
				EP.Proveedores_codigoProveedor = P_codP
                where codigoEmailProveedor = codEP;
		End $$
        
Delimiter ;

-- Reinicar contador ID Email proveedor --

Delimiter $$

	create procedure sp_reinicioIdEmailProveedor()
		Begin
			alter table EmailProveedor auto_increment = 1;
		End $$
        
Delimiter ; 

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- Llamado de procedimientos almacenados -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -

-- Agregar email proveedor --

call sp_AgregarEmailProveedor('juan.martinez@techinnovators.com', 'Ingeniero de software senior en Tech Innovators, una empresa de desarrollo de tecnología de punta.', 2);
call sp_AgregarEmailProveedor('maria.gomez@creativeagency.net', 'Directora creativa en Creative Agency, una agencia de publicidad y diseño gráfico.', 3);

-- Listar email proveedor --

call sp_ListarEmailProveedores();

-- Buscar email proveedor --

call sp_BuscarEmailProveedor(1);

-- Eliminar email proveedor --

call sp_EliminarEmailProveedor(1);

   -- Comprobacion --

	  call sp_ListarEmailProveedores();
      
-- Editar email proveedor --

call sp_EditarEmailProveedor(2, 'laura.lopez@greenfuture.org', 'Coordinadora de proyectos en Green Future, una organización sin fines de lucros.', 2);

	-- Comprobacion --
    
	   call sp_ListarEmailProveedores();
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --


-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Procedimientos almacenados -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Telefono proveedor -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- AGREGAR -- 

Delimiter $$

	create procedure sp_AgregarTelefonoProveedor(
    in numeroPrincipal varchar(50),
    in numeroSecundario varchar(100),
	in observaciones varchar(100),
    in Proveedores_codigoProveedor int)
		Begin
			Insert into TelefonoProveedor(
			numeroPrincipal,
     		numeroSecundario,
	 		observaciones,
     		Proveedores_codigoProveedor) values (
			numeroPrincipal,
     		numeroSecundario,
	 		observaciones,
     		Proveedores_codigoProveedor);
		End $$
    
Delimiter ;                  

-- LISTAR --

Delimiter $$

	create procedure sp_ListarTelefonoProveedor()
		Begin
			select
			TP.codigoTelefonoProveedor,
			TP.numeroPrincipal,
			TP.numeroSecundario,
            TP.observaciones,
			TP.Proveedores_codigoProveedor
            from TelefonoProveedor TP;
        End $$
        
Delimiter ;

-- BUSCAR -- 

Delimiter $$

	create procedure sp_BuscarTelefonoProveedor(in codTP int)
		Begin
			select
			TP.codigoTelefonoProveedor,
			TP.numeroPrincipal,
			TP.numeroSecundario,
            TP.observaciones,
			TP.Proveedores_codigoProveedor
            from TelefonoProveedor TP
            where codigoTelefonoProveedor = codTP;
        End $$
        
Delimiter ;

-- ELIMINAR --

Delimiter $$

	create procedure sp_EliminarTelefonoProveedor(in codTP int)
		Begin
			Delete from TelefonoProveedor
				where codigoTelefonoProveedor = codTP;
        End $$
        
Delimiter ;

-- EDITAR --

Delimiter $$

	create procedure sp_EditarTelefonoProveedor(in codTP int, in numP varchar(8), in numS varchar(8),
			in obser varchar(45), in P_codP int)
		Begin
			Update TelefonoProveedor TP
				set
				TP.numeroPrincipal = numP,
				TP.numeroSecundario = numS,
                TP.observaciones = obser,
                TP.Proveedores_codigoProveedor = P_codP
                where codigoTelefonoProveedor = codTP;
		End $$
        
Delimiter ;

-- Reinicar contador ID Telefono proveedor --

Delimiter $$

	create procedure sp_reinicioIdTelefonoProveedor()
		Begin
			alter table TelefonoProveedor auto_increment = 1;
		End $$
        
Delimiter ; 

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- Llamado de procedimientos almacenados -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -

-- Agregar telefono proveedor --

call sp_AgregarTelefonoProveedor('47841452', '49632514', 'Le sabe a matematica', 2);
call sp_AgregarTelefonoProveedor('35461872', '26531478', 'Tiene labia', 3);

-- Listar telefono proveedor --

call sp_ListarTelefonoProveedor();

-- Buscar telefono proveedor --

call sp_BuscarTelefonoProveedor(1);

-- Eliminar telefono proveedor --

call sp_EliminarTelefonoProveedor(1);

   -- Comprobacion --

	  call sp_ListarTelefonoProveedor();
      
-- Editar telefono proveedor --

call sp_EditarTelefonoProveedor(2, '49251075', '30154078', 'Tiene un traje elegante', 2);

	-- Comprobacion --
    
	   call sp_ListarTelefonoProveedor();
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --