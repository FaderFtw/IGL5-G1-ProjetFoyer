package tn.esprit.tpfoyer17.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChambreServiceTest {

    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private ChambreService chambreService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddChambre() {
        Chambre chambre = new Chambre();
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        Chambre result = chambreService.addChambre(chambre);

        assertEquals(chambre, result);
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    public void testGetAllChambres() {
        List<Chambre> mockChambres = new ArrayList<>();
        when(chambreRepository.findAll()).thenReturn(mockChambres);

        List<Chambre> result = chambreService.getAllChambres();

        assertEquals(mockChambres, result);
        verify(chambreRepository, times(1)).findAll();
    }

    @Test
    public void testGetChambreById() {
        long idChambre = 1L;
        Chambre mockChambre = new Chambre();
        when(chambreRepository.findById(idChambre)).thenReturn(Optional.of(mockChambre));

        Chambre result = chambreService.getChambreById(idChambre);

        assertEquals(mockChambre, result);
        verify(chambreRepository, times(1)).findById(idChambre);
    }

    @Test
    public void testDeleteChambre() {
        long idChambre = 1L;

        chambreService.deleteChambre(idChambre);

        verify(chambreRepository, times(1)).deleteById(idChambre);
    }

    @Test
    public void testUpdateChambre() {
        Chambre chambre = new Chambre();
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        Chambre result = chambreService.updateChambre(chambre);

        assertEquals(chambre, result);
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    public void testGetChambresParNomUniversite() {
        String nomUniversite = "Esprit";
        List<Chambre> mockChambres = new ArrayList<>();
        when(chambreRepository.findByBlocFoyerUniversiteNomUniversite(nomUniversite)).thenReturn(mockChambres);

        List<Chambre> result = chambreService.getChambresParNomUniversite(nomUniversite);

        assertEquals(mockChambres, result);
        verify(chambreRepository, times(1)).findByBlocFoyerUniversiteNomUniversite(nomUniversite);
    }

}

