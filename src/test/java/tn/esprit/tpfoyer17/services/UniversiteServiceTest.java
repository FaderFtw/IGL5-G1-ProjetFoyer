package tn.esprit.tpfoyer17.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer17.entities.Universite;
import tn.esprit.tpfoyer17.entities.Foyer;
import tn.esprit.tpfoyer17.repositories.FoyerRepository;
import tn.esprit.tpfoyer17.repositories.UniversiteRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UniversiteServiceTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @Mock
    private FoyerRepository foyerRepository;

    @InjectMocks
    private UniversiteService universiteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddUniversite() {
        Universite universite = new Universite();
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = universiteService.addUniversite(universite);

        assertEquals(universite, result);
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    public void testGetAllUniversites() {
        List<Universite> mockUniversites = new ArrayList<>();
        when(universiteRepository.findAll()).thenReturn(mockUniversites);

        List<Universite> result = universiteService.getAllUniversites();

        assertEquals(mockUniversites, result);
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    public void testGetUniversiteById() {
        long idUniversite = 1L;
        Universite mockUniversite = new Universite();
        when(universiteRepository.findById(idUniversite)).thenReturn(Optional.of(mockUniversite));

        Universite result = universiteService.getUniversiteById(idUniversite);

        assertEquals(mockUniversite, result);
        verify(universiteRepository, times(1)).findById(idUniversite);
    }

    @Test
    public void testGetUniversiteById_NotFound() {
        long idUniversite = 1L;
        when(universiteRepository.findById(idUniversite)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            universiteService.getUniversiteById(idUniversite);
        });

        assertEquals("Universite not found with id: " + idUniversite, exception.getMessage());
        verify(universiteRepository, times(1)).findById(idUniversite);
    }

    @Test
    public void testDeleteUniversite() {
        long idUniversite = 1L;

        universiteService.deleteUniversite(idUniversite);

        verify(universiteRepository, times(1)).deleteById(idUniversite);
    }

    @Test
    public void testUpdateUniversite() {
        Universite universite = new Universite();
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = universiteService.updateUniversite(universite);

        assertEquals(universite, result);
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    public void testAffecterFoyerAUniversite() {
        long idFoyer = 1L;
        String nomUniversite = "Test University";
        Universite mockUniversite = new Universite();
        Foyer mockFoyer = new Foyer();

        when(universiteRepository.findByNomUniversite(nomUniversite)).thenReturn(mockUniversite);
        when(foyerRepository.findById(idFoyer)).thenReturn(Optional.of(mockFoyer));
        when(universiteRepository.save(mockUniversite)).thenReturn(mockUniversite);

        Universite result = universiteService.affecterFoyerAUniversite(idFoyer, nomUniversite);

        assertEquals(mockFoyer, result.getFoyer());
        verify(universiteRepository, times(1)).findByNomUniversite(nomUniversite);
        verify(foyerRepository, times(1)).findById(idFoyer);
        verify(universiteRepository, times(1)).save(mockUniversite);
    }

    @Test
    public void testAffecterFoyerAUniversite_UniversiteNotFound() {
        long idFoyer = 1L;
        String nomUniversite = "NonExistentUniversity";

        when(universiteRepository.findByNomUniversite(nomUniversite)).thenReturn(null);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            universiteService.affecterFoyerAUniversite(idFoyer, nomUniversite);
        });

        assertEquals("Universite with name " + nomUniversite + " not Found", exception.getMessage());
        verify(universiteRepository, times(1)).findByNomUniversite(nomUniversite);
        verify(foyerRepository, never()).findById(anyLong());
        verify(universiteRepository, never()).save(any(Universite.class));
    }


    @Test
    public void testDesaffecterFoyerAUniversite() {
        long idUniversite = 1L;
        Universite mockUniversite = new Universite();
        mockUniversite.setFoyer(new Foyer());

        when(universiteRepository.findById(idUniversite)).thenReturn(Optional.of(mockUniversite));
        when(universiteRepository.save(mockUniversite)).thenReturn(mockUniversite);

        Universite result = universiteService.desaffecterFoyerAUniversite(idUniversite);

        assertNull(result.getFoyer());
        verify(universiteRepository, times(1)).findById(idUniversite);
        verify(universiteRepository, times(1)).save(mockUniversite);
    }
}
