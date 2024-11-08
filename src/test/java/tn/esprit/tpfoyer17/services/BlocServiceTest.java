package tn.esprit.tpfoyer17.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.repositories.BlocRepository;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlocServiceTest {

    @Mock
    private BlocRepository blocRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private BlocService blocService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddBloc() {
        Bloc bloc = new Bloc();
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.addBloc(bloc);

        assertEquals(bloc, result);
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    void testGetAllBlocs() {
        List<Bloc> mockBlocs = new ArrayList<>();
        when(blocRepository.findAll()).thenReturn(mockBlocs);

        List<Bloc> result = blocService.getAllBlocs();

        assertEquals(mockBlocs, result);
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testGetBlocById() {
        long idBloc = 1L;
        Bloc mockBloc = new Bloc();
        when(blocRepository.findById(idBloc)).thenReturn(Optional.of(mockBloc));

        Bloc result = blocService.getBlocById(idBloc);

        assertEquals(mockBloc, result);
        verify(blocRepository, times(1)).findById(idBloc);
    }

    @Test
    void testGetBlocById_NotFoundThrowsException() {
        long idBloc = 1L;
        when(blocRepository.findById(idBloc)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            blocService.getBlocById(idBloc);
        });

        assertEquals("Bloc with ID " + idBloc + " not found.", exception.getMessage());
        verify(blocRepository, times(1)).findById(idBloc);
    }


    @Test
    void testDeleteBloc() {
        long idBloc = 1L;

        blocService.deleteBloc(idBloc);

        verify(blocRepository, times(1)).deleteById(idBloc);
    }

    @Test
    void testUpdateBloc() {
        Bloc bloc = new Bloc();
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.updateBloc(bloc);

        assertEquals(bloc, result);
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    void testAffecterChambresABloc() {
        long idBloc = 1L;
        List<Long> chambreIds = List.of(1L, 2L, 3L);
        Bloc mockBloc = new Bloc();
        List<Chambre> mockChambres = new ArrayList<>();

        for (long chambreId : chambreIds) {
            Chambre chambre = new Chambre();
            chambre.setIdChambre(chambreId);
            mockChambres.add(chambre);
        }

        when(blocRepository.findById(idBloc)).thenReturn(Optional.of(mockBloc));
        when(chambreRepository.findAllById(chambreIds)).thenReturn(mockChambres);

        blocService.affecterChambresABloc(chambreIds, idBloc);

        for (Chambre chambre : mockChambres) {
            assertEquals(mockBloc, chambre.getBloc());
        }
        verify(blocRepository, times(1)).findById(idBloc);
        verify(chambreRepository, times(1)).findAllById(chambreIds);
        verify(blocRepository, times(1)).save(mockBloc);
    }

    @Test
    void testAffecterChambresABloc_BlocNotFound() {
        long idBloc = 1L;
        List<Long> chambreIds = List.of(1L, 2L, 3L);

        when(blocRepository.findById(idBloc)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            blocService.affecterChambresABloc(chambreIds, idBloc);
        });

        assertEquals("Bloc with ID " + idBloc + " not found", exception.getMessage());
        verify(blocRepository, times(1)).findById(idBloc);
        verify(chambreRepository, never()).findAllById(anyList());
    }

}
