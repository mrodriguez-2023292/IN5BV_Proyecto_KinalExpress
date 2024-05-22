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
		references CargoEmpleado(codigoCargoEmpleado)
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
		references Proveedores(codigoProveedor)
);

-- Se crea la tabla o entidad EmailProveedor con atributos: codigoEmailProveedor(PK), emailProveedor, descripcion, Proveedores_codigoProveedor(FK)
create table EmailProveedor(
	codigoEmailProveedor int not null auto_increment,
    emailProveedor varchar(50) not null,
    descripcion varchar(100) not null,
    Proveedores_codigoProveedor int not null,
    primary key PK_codigoEmailProveedor(codigoEmailProveedor),
    constraint FK_EmailProveedor_Proveedores foreign key EmailProveedor(Proveedores_codigoProveedor)
		references Proveedores(codigoProveedor)
);

-- Se crea la tabla o entidad Compras con atributos: numeroDocumento(PK), fechaDocumento, descripcion, totalDocumento
create table Compras(
	numeroDocumento int not null auto_increment,
    fechaDocumento date not null,
    descripcion varchar(60) not null,
    totalDocumento decimal(10,2),
    primary key PK_numeroDocumento(numeroDocumento)
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
    precioUnitario decimal(10,2),
    precioDocena decimal(10,2),
    precioMayor decimal(10,2),
    imagenProducto varchar(45),
    existencia int,
    TipoProducto_codigoTipoProducto int not null,
    Proveedores_codigoProveedor int not null,
    primary key PK_codigoProducto(codigoProducto),
    constraint FK_Productos_TipoProducto foreign key Productos(TipoProducto_codigoTipoProducto)
		references TipoProducto(codigoTipoProducto),
    constraint FK_Productos_Proveedores foreign key Productos(Proveedores_codigoProveedor)
		references Proveedores(codigoProveedor)
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
		references Compras(numeroDocumento),
	constraint FK_DetalleCompra_Productos foreign key DetalleCompra(Productos_codigoProducto)
		references Productos(codigoProducto)
);

-- Se crea la tabla o entidad Factura con atributos: numeroFactura(PK), estado, totalFactura, fechaFactura, Clientes_codigoCliente(FK), Empleados_codigoEmpleado(FK)
create table Factura(
	numeroFactura int not null auto_increment,
    estado varchar(50) not null,
    totalFactura decimal(10,2),
    fechaFactura varchar(45),
    Clientes_codigoCliente int not null,
    Empleados_codigoEmpleado int not null,
    primary key PK_numeroFactura(numeroFactura),
    constraint FK_Factura_Clientes foreign key Factura(Clientes_codigoCliente)
		references Clientes(codigoCliente),
    constraint FK_Factura_Empleados foreign key Factura(Empleados_codigoEmpleado)
		references Empleados(codigoEmpleado)
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
		references Factura(numeroFactura),
	constraint FK_DetalleFactura_Productos foreign key DetalleFactura(Productos_codigoProducto)
		references Productos(codigoProducto)
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
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Compras -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- AGREGAR -- 

Delimiter $$

	create procedure sp_AgregarCompras(
    in fechaDocumento date,
    in descripcion varchar(60),
    in totalDocumento decimal(10,2))
		Begin
			Insert into Compras(
			fechaDocumento,
			descripcion,
			totalDocumento) values (
			fechaDocumento,
			descripcion,
			totalDocumento);
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
	create procedure sp_EditarCompras(in numDoc int, in feDoc date, in descripcion varchar(60),
			in toDoc decimal(10,2))
		Begin
			Update Compras C
				set
				 C.fechaDocumento = feDoc,
				 C.descripcion = descripcion,
				 C.totalDocumento = toDoc
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

call sp_AgregarCompras('2002-10-01', 'Es el mejor peluche que vas a ver en tu vida', 1500.00);
call sp_AgregarCompras('2007-05-30', 'Compraste la espada de un maestro perdido', 13500.00);

-- Listar compras --

call sp_ListarCompras();

-- Buscar compras --

call sp_BuscarCompras(1);

-- Eliminar compras --

call sp_EliminarCompras(1);

   -- Comprobacion --

	  call sp_ListarCompras();
      
-- Editar compras --

call sp_EditarCompras(2, '2002-02-12', 'Es el terreneitor', 3000.00);

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