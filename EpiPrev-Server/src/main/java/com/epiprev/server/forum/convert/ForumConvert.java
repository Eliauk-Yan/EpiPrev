package com.epiprev.server.forum.convert;

import com.epiprev.server.forum.domain.dto.CommentDTO;
import com.epiprev.server.forum.domain.dto.PostDTO;
import com.epiprev.server.forum.domain.entity.ForumComment;
import com.epiprev.server.forum.domain.entity.ForumPost;
import com.epiprev.server.forum.domain.vo.CommentVO;
import com.epiprev.server.forum.domain.vo.PostVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ForumConvert {

    @Mapping(source = "userId", target = "authorId")
    @Mapping(source = "username", target = "authorName")
    PostVO toPostVO(ForumPost post);

    List<PostVO> toPostVOs(List<ForumPost> posts);

    ForumPost toEntity(PostDTO postDTO);

    ForumComment toEntity(CommentDTO commentDTO);

    @Mapping(source = "userId", target = "authorId")
    @Mapping(source = "username", target = "authorName")
    CommentVO toCommentVO(ForumComment comment);

    List<CommentVO> toCommentVOs(List<ForumComment> comments);

}
