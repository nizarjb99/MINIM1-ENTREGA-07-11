package edu.upc.dsa.services;

import edu.upc.dsa.Biblioteca;
import edu.upc.dsa.BibliotecaImpl;
import edu.upc.dsa.models.*;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/biblioteca", description = "Endpoints del servei de Biblioteca")
@Path("/biblioteca")
public class BibliotecaService {

    private final Biblioteca b;

    public BibliotecaService() {
        this.b = BibliotecaImpl.getInstance();
    }

    @POST
    @Path("/lector")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Afegeix un lector")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Creat", response = Lector.class),
            @ApiResponse(code = 400, message = "Dades incompletes")
    })
    public Response addLector(Lector lector) {
        if (lector == null || lector.getId() == null) return Response.status(400).entity("lector null o sense id").build();
        b.addLector(lector);
        return Response.status(201).entity(lector).build();
    }

    @GET
    @Path("/lectors")
    @ApiOperation(value = "Llista tots els lectors")
    public Response getLectors() {
        List<Lector> lectors = b.getLectors();
        GenericEntity<List<Lector>> entity = new GenericEntity<List<Lector>>(lectors){};
        return Response.ok(entity).build();
    }

    @POST
    @Path("/magatzem")
    @ApiOperation(value = "Afegeix un llibre al magatzem")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Creat", response = LlibreEmmagatzemat.class),
            @ApiResponse(code = 400, message = "Dades incompletes")
    })
    public Response addLlibreMagatzem(LlibreEmmagatzemat llibre) {
        if (llibre == null || llibre.getId() == null) return Response.status(400).entity("llibre null o sense id").build();
        b.addLlibreEmmagatzemat(llibre);
        return Response.status(201).entity(llibre).build();
    }

    @POST
    @Path("/catalogar/seguent")
    @ApiOperation(value = "Cataloga el seguent llibre del magatzem")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = LlibreCatalogat.class),
            @ApiResponse(code = 204, message = "Magatzem buit")
    })
    public Response catalogarSeguent() {
        LlibreCatalogat c = b.catalogarSeguent();
        if (c == null) return Response.status(204).build();
        return Response.ok(c).build();
    }

    @GET
    @Path("/cataleg")
    @ApiOperation(value = "Llista el cataleg")
    public Response getCataleg() {
        List<LlibreCatalogat> l = b.getCataleg();
        GenericEntity<List<LlibreCatalogat>> entity = new GenericEntity<List<LlibreCatalogat>>(l){};
        return Response.ok(entity).build();
    }

    @GET
    @Path("/cataleg/{isbn}")
    @ApiOperation(value = "Obtenir un llibre catalogat per ISBN")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = LlibreCatalogat.class),
            @ApiResponse(code = 404, message = "No trobat")
    })
    public Response getPerIsbn(@PathParam("isbn") String isbn) {
        LlibreCatalogat c = b.getLlibreCatalogat(isbn);
        if (c == null) return Response.status(404).entity("No trobat").build();
        return Response.ok(c).build();
    }

    @POST
    @Path("/prestec")
    @ApiOperation(value = "Crea un prestec si hi ha disponibilitat")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Creat", response = Prestec.class),
            @ApiResponse(code = 404, message = "Lector o ISBN no existeix"),
            @ApiResponse(code = 409, message = "Sense disponiblitat")
    })
    public Response prestar(@QueryParam("lectorId") String lectorId, @QueryParam("isbn") String isbn) {
        Prestec p = b.prestar(lectorId, isbn);
        if (p == null) {
            LlibreCatalogat c = b.getLlibreCatalogat(isbn);
            if (b.getLector(lectorId) == null || c == null) return Response.status(404).entity("lector o isbn inexistent").build();
            return Response.status(409).entity("sense disponibilitat").build();
        }
        return Response.status(201).entity(p).build();
    }

    @POST
    @Path("/retorn/{prestecId}")
    @ApiOperation(value = "Retorna un prestec")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Prestec no trobat")
    })
    public Response retornar(@PathParam("prestecId") String prestecId) {
        List<Prestec> abans = b.getPrestecsDeLector("");
        b.retornar(prestecId);
        return Response.ok().build();
    }

    @GET
    @Path("/prestecs/{lectorId}")
    @ApiOperation(value = "Tots els prestecs d’un lector")
    public Response getPrestecs(@PathParam("lectorId") String lectorId) {
        List<Prestec> l = b.getPrestecsDeLector(lectorId);
        GenericEntity<List<Prestec>> entity = new GenericEntity<List<Prestec>>(l){};
        return Response.ok(entity).build();
    }

    @GET
    @Path("/prestecs/enTramit/{lectorId}")
    @ApiOperation(value = "Prestecs en tramit")
    public Response getPrestecsEnTramit(@PathParam("lectorId") String lectorId) {
        List<Prestec> l = b.getPrestecsEnTramit(lectorId);
        GenericEntity<List<Prestec>> entity = new GenericEntity<List<Prestec>>(l){};
        return Response.ok(entity).build();
    }
}
