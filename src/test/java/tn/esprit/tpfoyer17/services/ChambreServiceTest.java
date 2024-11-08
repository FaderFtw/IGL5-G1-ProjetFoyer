package tn.esprit.tpfoyer17.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChambreServiceTest {

    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private ChambreService chambreService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddChambre() {
        Chambre chambre = new Chambre();
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        Chambre result = chambreService.addChambre(chambre);

        assertEquals(chambre, result);
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    void testGetAllChambres() {
        List<Chambre> mockChambres = new ArrayList<>();
        when(chambreRepository.findAll()).thenReturn(mockChambres);

        List<Chambre> result = chambreService.getAllChambres();

        assertEquals(mockChambres, result);
        verify(chambreRepository, times(1)).findAll();
    }

    @Test
    void testGetChambreById_NotFound() {
        long idChambre = 1L;
        when(chambreRepository.findById(idChambre)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            chambreService.getChambreById(idChambre);
        });

        assertEquals("Chambre not found with id: " + idChambre, exception.getMessage());
        verify(chambreRepository, times(1)).findById(idChambre);
    }

    @Test
    void testDeleteChambre() {
        long idChambre = 1L;

        chambreService.deleteChambre(idChambre);

        verify(chambreRepository, times(1)).deleteById(idChambre);
    }

    @Test
    void testUpdateChambre() {
        Chambre chambre = new Chambre();
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        Chambre result = chambreService.updateChambre(chambre);

        assertEquals(chambre, result);
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    void testGetChambresParNomUniversite() {
        String nomUniversite = "Esprit";
        List<Chambre> mockChambres = new ArrayList<>();
        when(chambreRepository.findByBlocFoyerUniversiteNomUniversite(nomUniversite)).thenReturn(mockChambres);

        List<Chambre> result = chambreService.getChambresParNomUniversite(nomUniversite);

        assertEquals(mockChambres, result);
        verify(chambreRepository, times(1)).findByBlocFoyerUniversiteNomUniversite(nomUniversite);
    }

    @Test
    void testGetChambresParBlocEtTypeKeyWord() {
        long idBloc = 1L;
        TypeChambre typeChambre = TypeChambre.SIMPLE; // Assuming SIMPLE is a valid TypeChambre
        List<Chambre> mockChambres = new ArrayList<>();
        when(chambreRepository.findByBlocIdBlocAndTypeChambre(idBloc, typeChambre)).thenReturn(mockChambres);

        List<Chambre> result = chambreService.getChambresParBlocEtTypeKeyWord(idBloc, typeChambre);

        assertEquals(mockChambres, result);
        verify(chambreRepository, times(1)).findByBlocIdBlocAndTypeChambre(idBloc, typeChambre);
    }

    @Test
    void testGetChambresParBlocEtTypeJPQL() {
        long idBloc = 1L;
        TypeChambre typeChambre = TypeChambre.SIMPLE;
        List<Chambre> mockChambres = new ArrayList<>();
        when(chambreRepository.findByBlocIdBlocAndTypeChambreJPQL(idBloc, typeChambre)).thenReturn(mockChambres);

        List<Chambre> result = chambreService.getChambresParBlocEtTypeJPQL(idBloc, typeChambre);

        assertEquals(mockChambres, result);
        verify(chambreRepository, times(1)).findByBlocIdBlocAndTypeChambreJPQL(idBloc, typeChambre);
    }

    @Test
    void testGetChambresNonReserveParNomUniversiteEtTypeChambre() {
        String nomUniversite = "Esprit";
        TypeChambre typeChambre = TypeChambre.SIMPLE;
        List<Chambre> mockChambres = new ArrayList<>();
        when(chambreRepository.getChambresNonReserveParNomUniversiteEtTypeChambre(nomUniversite, typeChambre)).thenReturn(mockChambres);

        List<Chambre> result = chambreService.getChambresNonReserveParNomUniversiteEtTypeChambre(nomUniversite, typeChambre);

        assertEquals(mockChambres, result);
        verify(chambreRepository, times(1)).getChambresNonReserveParNomUniversiteEtTypeChambre(nomUniversite, typeChambre);
    }

    @Test
    void testGetChambreNonReserver() {
        List<Chambre> mockChambres = new ArrayList<>();
        when(chambreRepository.getChambresNonReserve()).thenReturn(mockChambres);

        chambreService.getChambreNonReserver();

        verify(chambreRepository, times(1)).getChambresNonReserve();
    }
}
