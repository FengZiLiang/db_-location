package cn.td.aiot.location.service.impl;

import cn.td.aiot.location.dao.CharacterTypeMapper;
import cn.td.aiot.location.dao.EnclosureCharacterMapper;
import cn.td.aiot.location.dao.EnclosureMapper;
import cn.td.aiot.location.domain.CharacterType;
import cn.td.aiot.location.domain.Enclosure;
import cn.td.aiot.location.domain.EnclosureCharacter;
import cn.td.aiot.location.service.EnclosureCharacterService;
import cn.td.aiot.location.vo.EnclosureCharacterVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ClassName  EnclosureCharacterService <br/>
 * Description 电子围栏特性服务层 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/8/5 14:34<br/>
 * @since JDK 1.8
 */
@Service
public class EnclosureCharacterServiceImpl extends ServiceImpl<EnclosureCharacterMapper, EnclosureCharacter> implements EnclosureCharacterService {


    private final EnclosureCharacterMapper characterMapper;
    private final EnclosureMapper enclosureMapper;
    private final CharacterTypeMapper characterTypeMapper;

    public EnclosureCharacterServiceImpl(EnclosureCharacterMapper characterMapper, EnclosureMapper enclosureMapper, CharacterTypeMapper characterTypeMapper) {
        this.characterMapper = characterMapper;
        this.enclosureMapper = enclosureMapper;
        this.characterTypeMapper = characterTypeMapper;
    }

    @Override
    public List<EnclosureCharacterVo> findAllCharacter(Integer mapId) {
        List<EnclosureCharacterVo> characterVos = new ArrayList<>();
        try {

            QueryWrapper<Enclosure> idQuery = new QueryWrapper<>();
            idQuery.select("id");

            List<Enclosure> enclosureCharacters = enclosureMapper.selectList(idQuery);

            System.out.println("信息" + Arrays.toString(enclosureCharacters.toArray()));
            for (Enclosure enclosure : enclosureCharacters) {
                //得到围栏Id
                Integer id = enclosure.getId();
                //通过围栏id查询围栏的名字
                QueryWrapper<Enclosure> nameQuery = new QueryWrapper<>();
                nameQuery.select("enclosure_name");
                nameQuery.eq("id", id);
                nameQuery.eq("map_id", mapId);
                String enclosureName = enclosureMapper.selectOne(nameQuery).getEnclosureName();
                //通过围栏id获取围栏的特性
                QueryWrapper<EnclosureCharacter> typeQuery = new QueryWrapper<>();
                typeQuery.select("id", "character_type", "is_forever");
                typeQuery.eq("enclosure_id", id);
                List<EnclosureCharacter> characters = characterMapper.selectList(typeQuery);

                // 围栏特性Map映射
                List<List<Map<String, Object>>> features = new ArrayList<>();
                List<Map<String, Object>> list = new ArrayList<>();
                characters.forEach(character -> {
                    Map<String, Object> map = new HashMap<>(12);
                    Integer characterId = character.getId();
                    // 得到围栏的特性类型
                    Short characterType = character.getCharacterType();
                    //得到围栏是否是永久有效的
                    Boolean isForever = character.getIsForever();
                    //通过特性查询名字
                    QueryWrapper<CharacterType> nameType = new QueryWrapper<>();
                    nameType.select("character_name");
                    nameType.eq("character_type", characterType);
                    String characterName = characterTypeMapper.selectOne(nameType).getCharacterName();
                    map.put("id", characterId);
                    map.put("characterType", characterType);
                    map.put("characterName", characterName);
                    map.put("isForever", isForever);
                    list.add(map);
                });
                features.add(list);
                // 建造VO
                EnclosureCharacterVo characterVo = EnclosureCharacterVo.builder().enclosureId(id).enclosureName(enclosureName).features(features).build();
                characterVos.add(characterVo);
            }

            return characterVos.stream().distinct().collect(Collectors.toList());

        } catch (NullPointerException nullPointerException) {
            return characterVos;
        }

    }
}
