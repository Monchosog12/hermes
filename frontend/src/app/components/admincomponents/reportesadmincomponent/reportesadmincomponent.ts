import { Component } from '@angular/core';
interface ReporteUsuario {
  id: number;
  nombre: string;
  correo: string;
  fechaRegistro: string;
  estado: 'Activo' | 'Inactivo';
}

interface ReporteSolicitud {
  id: number;
  usuario: string;
  tipo: string;
  fecha: string;
  estado: 'Pendiente' | 'En proceso' | 'Atendida';
}

interface EstadisticaMensual {
  mes: string;
  cantidad: number;
}
@Component({
  selector: 'app-reportesadmincomponent',
  standalone: false,
  styleUrl: './reportesadmincomponent.css',
  templateUrl: './reportesadmincomponent.html',
})
export class Reportesadmincomponent { // Datos ficticios de usuarios
  usuarios: ReporteUsuario[] = [
    { id: 1, nombre: 'Laura Martínez', correo: 'laura@email.com', fechaRegistro: '2026-01-12', estado: 'Activo' },
    { id: 2, nombre: 'Carlos Rodríguez', correo: 'carlos@email.com', fechaRegistro: '2026-02-08', estado: 'Activo' },
    { id: 3, nombre: 'Mariana López', correo: 'mariana@email.com', fechaRegistro: '2026-03-15', estado: 'Inactivo' },
    { id: 4, nombre: 'Andrés Gómez', correo: 'andres@email.com', fechaRegistro: '2026-04-20', estado: 'Activo' },
    { id: 5, nombre: 'Sofía Torres', correo: 'sofia@email.com', fechaRegistro: '2026-05-03', estado: 'Activo' }
  ];

  // Datos ficticios de solicitudes
  solicitudes: ReporteSolicitud[] = [
    { id: 1001, usuario: 'Laura Martínez', tipo: 'Traducción de código', fecha: '2026-10-01', estado: 'Atendida' },
    { id: 1002, usuario: 'Carlos Rodríguez', tipo: 'Mejora de código', fecha: '2026-10-04', estado: 'Pendiente' },
    { id: 1003, usuario: 'Mariana López', tipo: 'Traducción de código', fecha: '2026-10-07', estado: 'En proceso' },
    { id: 1004, usuario: 'Andrés Gómez', tipo: 'Mejora de código', fecha: '2026-10-12', estado: 'Atendida' },
    { id: 1005, usuario: 'Sofía Torres', tipo: 'Traducción de código', fecha: '2026-10-18', estado: 'Pendiente' },
    { id: 1006, usuario: 'Laura Martínez', tipo: 'Mejora de código', fecha: '2026-10-22', estado: 'Atendida' },
    { id: 1007, usuario: 'Carlos Rodríguez', tipo: 'Traducción de código', fecha: '2026-10-25', estado: 'En proceso' },
    { id: 1008, usuario: 'Andrés Gómez', tipo: 'Mejora de código', fecha: '2026-10-28', estado: 'Atendida' }
  ];

  // Estadísticas mensuales ficticias
  estadisticasMensuales: EstadisticaMensual[] = [
    { mes: 'Jun', cantidad: 18 },
    { mes: 'Jul', cantidad: 25 },
    { mes: 'Ago', cantidad: 32 },
    { mes: 'Sep', cantidad: 28 },
    { mes: 'Oct', cantidad: 42 },
    { mes: 'Nov', cantidad: 35 }
  ];

  // Resumen de usuarios
  get totalUsuarios(): number {
    return this.usuarios.length;
  }

  get usuariosActivos(): number {
    return this.usuarios.filter(usuario => usuario.estado === 'Activo').length;
  }

  // Resumen de solicitudes
  get totalSolicitudes(): number {
    return this.solicitudes.length;
  }

  get solicitudesPendientes(): number {
    return this.solicitudes.filter(s => s.estado === 'Pendiente').length;
  }

  get solicitudesEnProceso(): number {
    return this.solicitudes.filter(s => s.estado === 'En proceso').length;
  }

  get solicitudesAtendidas(): number {
    return this.solicitudes.filter(s => s.estado === 'Atendida').length;
  }

  // Porcentajes para el gráfico circular
  get porcentajePendientes(): number {
    return this.totalSolicitudes
      ? (this.solicitudesPendientes / this.totalSolicitudes) * 100
      : 0;
  }

  get porcentajeEnProceso(): number {
    return this.totalSolicitudes
      ? (this.solicitudesEnProceso / this.totalSolicitudes) * 100
      : 0;
  }

  get porcentajeAtendidas(): number {
    return this.totalSolicitudes
      ? (this.solicitudesAtendidas / this.totalSolicitudes) * 100
      : 0;
  }

  // Altura de cada barra del gráfico mensual
  obtenerAlturaBarra(cantidad: number): number {
    const maximo = Math.max(...this.estadisticasMensuales.map(item => item.cantidad));
    return maximo > 0 ? (cantidad / maximo) * 100 : 0;
  }

  // Descargar reporte de usuarios
  descargarReporteUsuarios(): void {
    const encabezados = ['ID', 'Nombre', 'Correo', 'Fecha de registro', 'Estado'];

    const filas = this.usuarios.map(usuario => [
      usuario.id,
      usuario.nombre,
      usuario.correo,
      usuario.fechaRegistro,
      usuario.estado
    ]);

    this.descargarCSV('reporte_usuarios.csv', encabezados, filas);
  }

  // Descargar reporte de solicitudes
  descargarReporteSolicitudes(): void {
    const encabezados = ['ID', 'Usuario', 'Tipo de solicitud', 'Fecha', 'Estado'];

    const filas = this.solicitudes.map(solicitud => [
      solicitud.id,
      solicitud.usuario,
      solicitud.tipo,
      solicitud.fecha,
      solicitud.estado
    ]);

    this.descargarCSV('reporte_solicitudes.csv', encabezados, filas);
  }

  // Construir y descargar el archivo CSV
  private descargarCSV(
    nombreArchivo: string,
    encabezados: (string | number)[],
    filas: (string | number)[][]
  ): void {
    const contenido = [encabezados, ...filas]
      .map(fila =>
        fila.map(valor => `"${String(valor).replace(/"/g, '""')}"`).join(';')
      )
      .join('\n');

    const archivo = new Blob(
      ['\uFEFF' + contenido],
      { type: 'text/csv;charset=utf-8;' }
    );

    const url = URL.createObjectURL(archivo);
    const enlace = document.createElement('a');

    enlace.href = url;
    enlace.download = nombreArchivo;
    enlace.click();

    URL.revokeObjectURL(url);
  }}
